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

class ServerSettingsResponseTest {

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SERVER_SETTINGS, response.getCommand());
        ServerSettingsResponse serverSettingsResponse = new ServerSettingsResponse(response);
        assertEquals("localhost", serverSettingsResponse.getIp());
        assertEquals(5555, serverSettingsResponse.getPort());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SERVER_SETTINGS, response.getCommand());
        serverSettingsResponse = new ServerSettingsResponse(response);
        assertEquals("127.168.0.1", serverSettingsResponse.getIp());
        assertEquals(1, serverSettingsResponse.getPort());
    }

    @DisplayName("Test with string port")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParseWithWrongPort(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(2)));
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> new ServerSettingsResponse(response));

        //then
        assertEquals("For input string: \"port\"", e.getMessage());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(ServerSettingsResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ServerSettingsResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(new ArrayList<>(
                Arrays.asList("<KPT>52$1$localhost$5555</KPT>", "<KPT>52$1$127.168.0.1$1</KPT>",
                        "<KPT>52$1$localhost$port</KPT>"))));
    }
}