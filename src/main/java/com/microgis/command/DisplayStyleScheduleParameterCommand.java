package com.microgis.command;

import com.microgis.request.DisplayStyleScheduleParametersRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class DisplayStyleScheduleParameterCommand extends Command {

    public DisplayStyleScheduleParameterCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        DisplayStyleScheduleParametersRequest displayStyleScheduleParametersRequest = new DisplayStyleScheduleParametersRequest.Builder()
                .addPacketNumber(1)
                .addMinStopName(data[0])
                .addRightEndField(data[1])
                .addMinIndent(data[2])
                .addStartPictogram(data[3])
                .addSkipBeforePictogram(data[4])
                .addStartTime(data[5])
                .addFieldTimeWidth(data[6])
                .addDistanceBetweenTimesFieldsh(data[7])
                .addFontIndexForRouteAndUltimate(data[8])
                .addFontIndexForTimeAndArrived(data[9])
                .addFontIndexForUltimate(data[10])
                .addFontIndexForTicker(data[11])
                .addInterSymbolInterval(data[12])
                .addSpaceWidthUltimate(data[13])
                .addInterSymbolTimeInterval(data[14])
                .addSpaceWidthTime(data[15])
                .addAlignmentTime(data[16])
                .addSeparatorCharacterInscriptions(data[17])
                .addLanguageCount(data[18])
                .addControlBitsConclusion(data[19])
                .addDurationOutputSecondArrival(data[20])
                .addDurationShowPictogram(data[21])
                .build();
        String request = displayStyleScheduleParametersRequest.toString();
        writer.print(request);
    }
}
