package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class DisplayStyleParametersResponseTest {

    @Test
    void testParse() {
        //when
        Response response = new Response(new StringBuilder(
                "<KPT>306$1$47$1$1$0$2$99$1$5$8$1$15$5$0$30$10$0$8$8$8$12422$120$0$0$0$0$9$0$1$0$0$46$9$0$1$2$3$4</KPT>"));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_DISPLAY_STYLE_PARAMETERS, response.getCommand());
        DisplayStyleParametersResponse displayStyleParametersResponse = new DisplayStyleParametersResponse(response);
        assertEquals(47, displayStyleParametersResponse.getOutputControlBits().intValue());
        assertEquals(1, displayStyleParametersResponse.getProhibitionToDisplayTime().intValue());
        assertEquals(1, displayStyleParametersResponse.getLineOutputMode().intValue());
        assertEquals(0, displayStyleParametersResponse.getDisplayModeTime().intValue());
        assertEquals(2, displayStyleParametersResponse.getArrivalForecastAccuracy().intValue());
        assertEquals(99, displayStyleParametersResponse.getMaxValueArrival().intValue());
        assertEquals(1, displayStyleParametersResponse.getForecastAccuracy().intValue());
        assertEquals(5, displayStyleParametersResponse.getSwitchingInterval().intValue());
        assertEquals(8, displayStyleParametersResponse.getReadInterval().intValue());
        assertEquals(1, displayStyleParametersResponse.getConsiderQuantityLines().intValue());
        assertEquals(15, displayStyleParametersResponse.getMaxDisplayTime().intValue());
        assertEquals(5, displayStyleParametersResponse.getMinDisplayTime().intValue());
        assertEquals(0, displayStyleParametersResponse.getExpirationTime().intValue());
        assertEquals(30, displayStyleParametersResponse.getIntervalBetweenConnectionsServer().intValue());
        assertEquals(10, displayStyleParametersResponse.getWaitBeforeBitOutput().intValue());
        assertEquals(0, displayStyleParametersResponse.getInformationVerticalLines().intValue());
        assertEquals(8, displayStyleParametersResponse.getInformationHeightLinesDots().intValue());
        assertEquals(8, displayStyleParametersResponse.getStartLineHeight().intValue());
        assertEquals(8, displayStyleParametersResponse.getScheduleLineHeight().intValue());
        assertEquals(12422, displayStyleParametersResponse.getConstantBitFlags().intValue());
        assertEquals(120, displayStyleParametersResponse.getHorizontalLocationTime().intValue());
        assertEquals(0, displayStyleParametersResponse.getVerticalLocationTime().intValue());
        assertEquals(0, displayStyleParametersResponse.getFontIndexOutput().intValue());
        assertEquals(0, displayStyleParametersResponse.getConstantBitFlagsDisplaying().intValue());
        assertEquals(0, displayStyleParametersResponse.getHorizontalCoordinateDate().intValue());
        assertEquals(9, displayStyleParametersResponse.getVerticalCoordinateDate().intValue());
        assertEquals(0, displayStyleParametersResponse.getFontIndexOutputDates0().intValue());
        assertEquals(1, displayStyleParametersResponse.getDateDisplayLanguage0().intValue());
        assertEquals(0, displayStyleParametersResponse.getCaseTextOutput0().intValue());
        assertEquals(0, displayStyleParametersResponse.getConstantBitFlagsDisplayingFirstDate().intValue());
        assertEquals(46, displayStyleParametersResponse.getHorizontalLocationDate().intValue());
        assertEquals(9, displayStyleParametersResponse.getVerticalLocationDate().intValue());
        assertEquals(0, displayStyleParametersResponse.getFontIndexOutputDates1().intValue());
        assertEquals(1, displayStyleParametersResponse.getDateDisplayLanguage1().intValue());
        assertEquals(2, displayStyleParametersResponse.getCaseTextOutput1().intValue());
        assertEquals(3, displayStyleParametersResponse.getDistanceBetweenPoints().intValue());
        assertEquals(4, displayStyleParametersResponse.getNumberBetweenSpaces().intValue());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(DisplayStyleParametersResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(DisplayStyleParametersResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}