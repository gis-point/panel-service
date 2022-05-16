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
import static org.junit.jupiter.params.provider.Arguments.arguments;

class LogicalNumberResponseTest {

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.READ_LOGICAL_NUMBER, response.getCommand());
        LogicalNumberResponse logicalNumberResponse = new LogicalNumberResponse(response);
        assertEquals(444, logicalNumberResponse.getCurrentNumber());
        assertEquals(333, logicalNumberResponse.getDeterminedNumber());
        assertEquals(555, logicalNumberResponse.getPhysicalNumber());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.READ_LOGICAL_NUMBER, response.getCommand());
        logicalNumberResponse = new LogicalNumberResponse(response);
        assertEquals(1111, logicalNumberResponse.getCurrentNumber());
        assertEquals(2, logicalNumberResponse.getDeterminedNumber());
        assertEquals(1, logicalNumberResponse.getPhysicalNumber());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(LogicalNumberResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(LogicalNumberResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }

    private static Stream<Arguments> packetProvider() {
        return Stream
                .of(arguments(new ArrayList<>(Arrays.asList("<KPT>405$1$444$555$333</KPT>", "<KPT>405$1$1111$1$2</KPT>"))));
    }
}