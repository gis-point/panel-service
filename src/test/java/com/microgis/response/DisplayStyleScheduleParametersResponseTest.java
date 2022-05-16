package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class DisplayStyleScheduleParametersResponseTest {

    @Test
    void testParse() {
        //when
        Response response = new Response(
                new StringBuilder("<KPT>304$1$21$94$2$91$1$98$22$3$1$1$1$1$1$2$1$1$2$9$3$3$1$0</KPT>"));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS, response.getCommand());
        DisplayStyleScheduleParametersResponse displayStyleScheduleParametersResponse = new DisplayStyleScheduleParametersResponse(
                response);
        assertEquals(21, displayStyleScheduleParametersResponse.getMinStopName().intValue());
        assertEquals(94, displayStyleScheduleParametersResponse.getRightEndField().intValue());
        assertEquals(2, displayStyleScheduleParametersResponse.getMinIndent().intValue());
        assertEquals(91, displayStyleScheduleParametersResponse.getStartPictogram().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getSkipBeforePictogram().intValue());
        assertEquals(98, displayStyleScheduleParametersResponse.getStartTime().intValue());
        assertEquals(22, displayStyleScheduleParametersResponse.getFieldTimeWidth().intValue());
        assertEquals(3, displayStyleScheduleParametersResponse.getDistanceBetweenTimesFields().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getFontIndexForRouteAndUltimate().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getFontIndexForTimeAndArrived().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getFontIndexForUltimate().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getFontIndexForTicker().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getInterSymbolInterval().intValue());
        assertEquals(2, displayStyleScheduleParametersResponse.getSpaceWidthUltimate().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getInterSymbolTimeInterval().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getSpaceWidthTime().intValue());
        assertEquals(2, displayStyleScheduleParametersResponse.getAlignmentTime().intValue());
        assertEquals(9, displayStyleScheduleParametersResponse.getSeparatorCharacterInscriptions().intValue());
        assertEquals(3, displayStyleScheduleParametersResponse.getLanguageCount().intValue());
        assertEquals(3, displayStyleScheduleParametersResponse.getControlBitsConclusion().intValue());
        assertEquals(1, displayStyleScheduleParametersResponse.getDurationOutputSecondArrival().intValue());
        assertEquals(0, displayStyleScheduleParametersResponse.getDurationShowPictogram().intValue());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(DisplayStyleScheduleParametersResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(DisplayStyleScheduleParametersResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}