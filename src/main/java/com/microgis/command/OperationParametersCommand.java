package com.microgis.command;

import com.microgis.request.OperationParametersRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class OperationParametersCommand extends Command {

    public OperationParametersCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        OperationParametersRequest operationParametersRequest = new OperationParametersRequest.Builder()
                .addPacketNumber(1)
                .addSortMode(data[0])
                .addServerTimeout(data[1])
                .addKeepRouteRecordTime(data[2])
                .addStartEveningTime(data[3])
                .addFinishEveningTime(data[4])
                .addAdditionalServerInterval(data[5])
                .addAdditionalServerConnectionInterval(data[6])
                .build();
        String request = operationParametersRequest.toString();
        writer.print(request);
    }
}
