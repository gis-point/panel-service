package com.microgis.command;

import com.microgis.request.StopNameRequest;
import com.microgis.response.Response;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class StopNameCommand extends Command {

    public StopNameCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        String stopName = Response.indexInBound(data, 0) != null ? data[0] : null;
        if (stopName != null) {
            StopNameRequest stopNameRequest = new StopNameRequest.Builder()
                    .addPacketNumber(1)
                    .addStopName(stopName)
                    .build();
            String request = stopNameRequest.toString();
            writer.print(request);
        }
    }
}