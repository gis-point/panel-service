package com.microgis.command;

import com.microgis.controller.dto.panel.Panel;
import com.microgis.request.ServerSettingsRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;

public class ServerSettingsCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSettingsCommand.class);

    public ServerSettingsCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        if (data.length == 2 && !StringUtils.isEmpty(data[0]) && !StringUtils.isEmpty(data[1])) {
            String ip = data[0];
            String port = data[1];
            Panel panel = handler.getPanel();
            ServerSettingsRequest request = new ServerSettingsRequest.Builder()
                    .addPacketNumber(1)
                    .addIP(ip)
                    .addPort(port)
                    .build();
            String req = request.toString();
            writer.print(req);
            panel.setPort(port);
            panel.setIp(ip);
            panel.setTime(simpleDateFormat.format(new Date()));
        } else {
            LOGGER.debug("Bad server ip and port!!!!!");
        }
    }
}
