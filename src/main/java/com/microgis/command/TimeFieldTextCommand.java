package com.microgis.command;

import com.microgis.request.TimeFieldTextRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class TimeFieldTextCommand extends Command {

    public TimeFieldTextCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        TimeFieldTextRequest timeFieldTextRequest = new TimeFieldTextRequest.Builder()
                .addPacketNumber(1)
                .addNumber(data[0])
                .addText(data[1])
                .build();
        String request = timeFieldTextRequest.toString();
        writer.print(request);
    }
}