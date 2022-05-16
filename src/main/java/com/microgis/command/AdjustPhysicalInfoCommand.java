package com.microgis.command;

import com.microgis.request.AdjustPhysicalInfoRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class AdjustPhysicalInfoCommand extends Command {

    public AdjustPhysicalInfoCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        AdjustPhysicalInfoRequest adjustPhysicalInfoRequest = new AdjustPhysicalInfoRequest.Builder()
                .addPacketNumber(1)
                .addTextSpeed(Integer.parseInt(data[0]))
                .addMaxBrightness(Integer.parseInt(data[1]))
                .addMinBrightness(Integer.parseInt(data[2]))
                .build();
        String request = adjustPhysicalInfoRequest.toString();
        writer.print(request);
    }
}