package com.microgis.command;

import com.microgis.request.LogicalNumberRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class LogicalNumberCommand extends Command {

    public LogicalNumberCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        LogicalNumberRequest logicalNumberRequest = new LogicalNumberRequest.Builder()
                .addPacketNumber(1)
                .addLogicalNumber(Integer.parseInt(data[0]))
                .build();
        String request = logicalNumberRequest.toString();
        writer.print(request);
    }
}
