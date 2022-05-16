package com.microgis.command;

import com.microgis.request.AdditionalServerSettingsTimeoutRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class AdditionalServerSettingsTimeoutCommand extends Command {

    public AdditionalServerSettingsTimeoutCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        AdditionalServerSettingsTimeoutRequest request = new AdditionalServerSettingsTimeoutRequest.Builder()
                .addPacketNumber(1)
                .addTimeout(data[0])
                .build();
        writer.print(request.toString());
    }
}