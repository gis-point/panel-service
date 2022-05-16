package com.microgis.command;

import com.microgis.request.GprsRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class GprsCommand extends Command {

    public GprsCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        GprsRequest gprsRequest = new GprsRequest.Builder()
                .addPacketNumber(1)
                .addAddress(data[0])
                .addLogin(data[1])
                .addPassword(data[2])
                .build();
        String request = gprsRequest.toString();
        writer.print(request);
    }
}
