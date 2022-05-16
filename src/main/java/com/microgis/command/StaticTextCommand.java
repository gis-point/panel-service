package com.microgis.command;

import com.microgis.controller.dto.RouteResponse;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.request.StaticTextRequest;
import com.microgis.response.Response;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class StaticTextCommand extends Command {

    public StaticTextCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... text) {
        Panel panel = handler.getPanel();
        StaticTextRequest staticTextRequest = new StaticTextRequest.Builder()
                .addPacketNumber(1)
                .addText(text[0])
                .build();
        String request = staticTextRequest.toString();
        writer.print(request);
        fillRouteResponse(panel, Response.indexInBound(text, 0));
    }

    private void fillRouteResponse(Panel panel, String text) {
        panel.getRouteResponses().clear();
        panel.getRouteResponses().add(new RouteResponse(text));
    }
}