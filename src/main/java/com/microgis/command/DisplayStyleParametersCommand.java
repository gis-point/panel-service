package com.microgis.command;

import com.microgis.request.DisplayStyleParametersRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class DisplayStyleParametersCommand extends Command {

    public DisplayStyleParametersCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        DisplayStyleParametersRequest displayStyleParametersRequest = new DisplayStyleParametersRequest.Builder()
                .addPacketNumber(1)
                .addOutputControlBits(data[0])
                .addProhibitionToDisplayTime(data[1])
                .addLineOutputMode(data[2])
                .addDisplayModeTime(data[3])
                .addArrivalForecastAccuracy(data[4])
                .addMaxValueArrival(data[5])
                .addForecastAccuracy(data[6])
                .addSwitchingInterval(data[7])
                .addReadIntervals(data[8])
                .addConsiderQuantityLines(data[9])
                .addMaxDisplayTime(data[10])
                .addMinDisplayTime(data[11])
                .addExpirationTime(data[12])
                .addIntervalBetweenConnectionsServer(data[13])
                .addWitBeforeBitOutput(data[14])
                .addInformationVerticalLines(data[15])
                .addInformationHeightLinesDots(data[16])
                .addStartLineHeight(data[17])
                .addScheduleLineHeight(data[18])
                .addConstantBitFlags(data[19])
                .addHorizontalLocationTime(data[20])
                .addVerticalLocationTime(data[21])
                .addFontIndexOutput(data[22])
                .addConstantBitFlagsDisplaying(data[23])
                .addHorizontalCoordinateDate(data[24])
                .addVerticalCoordinateDate(data[25])
                .addFontIndexOutputDates0(data[26])
                .addDateDisplayLanguage0(data[27])
                .addCaseTextOutput0(data[28])
                .addConstantBitFlagsDisplayingFirstDate(data[29])
                .addHorizontalLocationDate(data[30])
                .addVerticalLocationDate(data[31])
                .addFontIndexOutputDates1(data[32])
                .addDateDisplayLanguage1(data[33])
                .addCaseTextOutput1(data[34])
                .addDistanceBetweenPoints(data[35])
                .addNumberBetweenSpaces(data[36])
                .build();
        String request = displayStyleParametersRequest.toString();
        writer.print(request);
    }
}
