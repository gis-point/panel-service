package com.microgis.command;

import com.microgis.request.ComplexCommunicationParametersRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class ComplexCommunicationParametersCommand extends Command {

    public ComplexCommunicationParametersCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        ComplexCommunicationParametersRequest complexCommunicationParametersRequest = new ComplexCommunicationParametersRequest.Builder()
                .addPacketNumber(1)
                .addAddress(data[0])
                .addLogin(data[1])
                .addPassword(data[2])
                .addServerAddress(data[3])
                .addServerPort(data[4])
                .build();
        String request = complexCommunicationParametersRequest.toString();
        writer.print(request);
    }
}
