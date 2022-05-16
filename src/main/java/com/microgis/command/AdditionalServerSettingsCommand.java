package com.microgis.command;

import com.microgis.request.AdditionalServerSettingsRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.Constants;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.springframework.util.StringUtils;

public class AdditionalServerSettingsCommand extends Command {

    public AdditionalServerSettingsCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        if (data.length == 2 && !StringUtils.isEmpty(data[0]) && !StringUtils.isEmpty(data[1])) {
            AdditionalServerSettingsRequest request = new AdditionalServerSettingsRequest.Builder()
                    .addPacketNumber(1)
                    .addIP(data[0])
                    .addPort(data[1])
                    .build();
            writer.print(request.toString());
        } else {
            writer.print(Constants.RESET_ADDITIONAL_SERVER);
        }
    }
}