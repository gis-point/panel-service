package com.microgis.command;

import com.microgis.controller.dto.panel.Panel;
import com.microgis.controller.dto.prediction.Prediction;
import com.microgis.request.IdentificationRequest;
import com.microgis.request.StopNameRequest;
import com.microgis.response.Response;
import com.microgis.response.TicketResponse;
import com.microgis.server.PanelHandler;
import com.microgis.server.Server;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AuthenticationCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationCommand.class);

    public AuthenticationCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) throws IOException {
        Server server = handler.getServer();

        IdentificationRequest identification = new IdentificationRequest.Builder().addPacketNumber(1).addDate(new Date())
                .build();
        writer.print(identification.toString());

        Response response = reader.read();

        if (response.getCommand().equals(com.microgis.controller.dto.panel.Command.TICKET)) {
            TicketResponse ticketResponse = new TicketResponse(response);
            int logicalNumber = ticketResponse.getLogicalNumber();
            Panel panel = new Panel();
            panel.setPhysicalNumber(ticketResponse.getPhysicalPanelNumber());
            panel.setLogicalNumber(logicalNumber);
            panel.setStopId(logicalNumber);
            panel.setIdentified(true);
            handler.setPanel(panel);
        }

        Panel panel = handler.getPanel();
        LOGGER.info("Authentication started for the panel - {}", panel.getLogicalNumber());
        List<Prediction> routes = server.getPredictionService().getPredictions(panel.getStopId());

        if (CollectionUtils.isEmpty(routes)) {
            LOGGER.info("Authentication failed for the panel {}, because predictions ZERO", panel.getLogicalNumber());
            findStopName(handler, panel);
            return;
        }

        String stopName = routes.get(0).getStopName();
        setStopName(stopName, panel);
    }

    /**
     * Write stop name into current panel
     *
     * @param stopName stop name
     * @param panel    current panel
     */
    private void setStopName(String stopName, Panel panel) {
        StopNameRequest stopNameRequest = new StopNameRequest.Builder()
                .addPacketNumber(1)
                .addStopName(stopName)
                .build();
        LOGGER.info("Set stopName {}", stopNameRequest);
        writer.print(stopNameRequest.toString());
        panel.setRunningLineText(stopName);
        panel.setStopName(stopName);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        panel.setTime(simpleDateFormat.format(new Date()));
    }

    /**
     * Finds stop name in case when authentication was failed
     */
    private void findStopName(PanelHandler handler, Panel panel) {
        handler.getServer().getModeResponses().stream()
                .filter(value -> value.getNumber() == panel.getLogicalNumber())
                .findFirst()
                .ifPresent(value -> setStopName(value.getStopName(), panel));
    }
}