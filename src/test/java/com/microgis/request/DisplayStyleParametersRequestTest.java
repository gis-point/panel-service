package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisplayStyleParametersRequestTest {

    @Test
    public void build() {
        DisplayStyleParametersRequest request = new DisplayStyleParametersRequest.Builder().addPacketNumber(1)
                .addOutputControlBits("47").addProhibitionToDisplayTime("1").addLineOutputMode("1").addDisplayModeTime("0")
                .addArrivalForecastAccuracy("2").addMaxValueArrival("99").addForecastAccuracy("1").addSwitchingInterval("5")
                .addReadIntervals("8").addConsiderQuantityLines("1").addMaxDisplayTime("15").addMinDisplayTime("5")
                .addExpirationTime("0").addIntervalBetweenConnectionsServer("30").addWitBeforeBitOutput("10")
                .addInformationVerticalLines("0").addInformationHeightLinesDots("8").addStartLineHeight("8")
                .addScheduleLineHeight("8").addConstantBitFlags("12422").addHorizontalLocationTime("120")
                .addVerticalLocationTime("0").addFontIndexOutput("0").addConstantBitFlagsDisplaying("0")
                .addHorizontalCoordinateDate("0").addVerticalCoordinateDate("9").addFontIndexOutputDates0("0")
                .addDateDisplayLanguage0("1").addCaseTextOutput0("0").addConstantBitFlagsDisplayingFirstDate("0")
                .addHorizontalLocationDate("46").addVerticalLocationDate("9").addFontIndexOutputDates1("0")
                .addDateDisplayLanguage1("1").addCaseTextOutput1("2").addDistanceBetweenPoints("3").addNumberBetweenSpaces("4")
                .build();
        assertEquals("<KPT>306$1$47$1$1$0$2$99$1$5$8$1$15$5$0$30$10$0$8$8$8$12422$120$0$0$0$0$9$0$1$0$0$46$9$0$1$2$3$4</KPT>",
                request.toString());
    }
}