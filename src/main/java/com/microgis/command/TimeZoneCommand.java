package com.microgis.command;

import com.microgis.request.TimeZoneRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class TimeZoneCommand extends Command {

    public TimeZoneCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        TimeZoneRequest timeZoneRequest = new TimeZoneRequest.Builder()
                .addPacketNumber(1)
                .addSummerTimeMode(data[0])
                .addTimeZone(data[1])
                .build();
        String request = timeZoneRequest.toString();
        writer.print(request);
    }
}
