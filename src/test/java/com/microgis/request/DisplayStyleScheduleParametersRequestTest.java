package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisplayStyleScheduleParametersRequestTest {

    @Test
    public void build() {
        DisplayStyleScheduleParametersRequest request = new DisplayStyleScheduleParametersRequest.Builder().addPacketNumber(1)
                .addMinStopName("21").addRightEndField("94").addMinIndent("2").addStartPictogram("91")
                .addSkipBeforePictogram("1").addStartTime("98").addFieldTimeWidth("22").addDistanceBetweenTimesFieldsh("3")
                .addFontIndexForRouteAndUltimate("1").addFontIndexForTimeAndArrived("1").addFontIndexForUltimate("1")
                .addFontIndexForTicker("1").addInterSymbolInterval("1").addSpaceWidthUltimate("2")
                .addInterSymbolTimeInterval("1").addSpaceWidthTime("1").addAlignmentTime("2")
                .addSeparatorCharacterInscriptions("9").addLanguageCount("3").addControlBitsConclusion("3")
                .addDurationOutputSecondArrival("1").addDurationShowPictogram("0").build();
        assertEquals("<KPT>304$1$21$94$2$91$1$98$22$3$1$1$1$1$1$2$1$1$2$9$3$3$1$0</KPT>", request.toString());
    }
}