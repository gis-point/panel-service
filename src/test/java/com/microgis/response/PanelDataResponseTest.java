package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PanelDataResponseTest {

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_PHYSICAL_INFO, response.getCommand());
        PanelDataResponse panelDataResponse = new PanelDataResponse(response);
        assertEquals(59, panelDataResponse.getTextSpeed());
        assertEquals(255, panelDataResponse.getMaxBrightness());
        assertEquals(10, panelDataResponse.getMinBrightness());
        assertTrue(panelDataResponse.isAutomaticMode());
        assertEquals(128, panelDataResponse.getCurrentBrightness());
        assertEquals(0, panelDataResponse.getCurrentIllumination());
        assertFalse(panelDataResponse.isTemperatureMode());
        assertEquals(0, panelDataResponse.getCurrentTemperature(), 0);

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_PHYSICAL_INFO, response.getCommand());
        panelDataResponse = new PanelDataResponse(response);
        assertEquals(59, panelDataResponse.getTextSpeed());
        assertEquals(255, panelDataResponse.getMaxBrightness());
        assertEquals(10, panelDataResponse.getMinBrightness());
        assertFalse(panelDataResponse.isAutomaticMode());
        assertEquals(128, panelDataResponse.getCurrentBrightness());
        assertEquals(1, panelDataResponse.getCurrentIllumination());
        assertTrue(panelDataResponse.isTemperatureMode());
        assertEquals(12.0, panelDataResponse.getCurrentTemperature(), 0);

    }

    @DisplayName("Test with wrong type")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParseWithWrongPort(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(2)));
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> new PanelDataResponse(response));

        //then
        assertEquals("Can't convert string to boolean", e.getMessage());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(PanelDataResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(PanelDataResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(new ArrayList<>(
                Arrays.asList("<KPT>302$1$59$255$10$1$128$0$0$0</KPT>", "<KPT>302$1$59$255$10$0$128$1$1$12.0</KPT>",
                        "<KPT>302$1$59$255$10$1$128$0$3$2</KPT>"))));
    }
}