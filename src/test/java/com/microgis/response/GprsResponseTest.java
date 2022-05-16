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
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GprsResponseTest {

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_GPRS, response.getCommand());
        GprsResponse gprsResponse = new GprsResponse(response);
        assertEquals("www.test.com", gprsResponse.getAddress());
        assertEquals("login", gprsResponse.getLogin());
        assertEquals("password", gprsResponse.getPassword());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_GPRS, response.getCommand());
        gprsResponse = new GprsResponse(response);
        assertEquals("www.test.com", gprsResponse.getAddress());
        assertNull(gprsResponse.getLogin());
        assertNull(gprsResponse.getPassword());
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(new ArrayList<>(
                Arrays.asList("<KPT>50$1$www.test.com$login$password</KPT>", "<KPT>50$1$www.test.com$$</KPT>"))));
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(GprsResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(GprsResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}