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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class AdditionalServerSettingsResponseTest {

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SERVER_SETTINGS, response.getCommand());
        AdditionalServerSettingsResponse additionalServerSettingsResponse = new AdditionalServerSettingsResponse(response);
        assertEquals("localhost", additionalServerSettingsResponse.getIp());
        assertEquals(5555, additionalServerSettingsResponse.getPort());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SERVER_SETTINGS, response.getCommand());
        additionalServerSettingsResponse = new AdditionalServerSettingsResponse(response);
        assertEquals("127.168.0.1", additionalServerSettingsResponse.getIp());
        assertEquals(1, additionalServerSettingsResponse.getPort());
    }

    @DisplayName("Test with string port")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParseWithWrongPort(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(2)));
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> new AdditionalServerSettingsResponse(response));

        //then
        assertEquals("For input string: \"port\"", e.getMessage());
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(new ArrayList<>(
                Arrays.asList("<KPT>52$1$localhost$5555</KPT>", "<KPT>52$1$127.168.0.1$1</KPT>",
                        "<KPT>52$1$localhost$port</KPT>"))));
    }


    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(AdditionalServerSettingsResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(AdditionalServerSettingsResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}