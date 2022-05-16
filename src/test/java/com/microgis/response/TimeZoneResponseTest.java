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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TimeZoneResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_TIME_ZONE, response.getCommand());
        TimeZoneResponse timeZoneResponse = new TimeZoneResponse(response);
        assertTrue(timeZoneResponse.isSummerTimeMode());
        assertEquals(2, timeZoneResponse.getTimeZone());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_TIME_ZONE, response.getCommand());
        timeZoneResponse = new TimeZoneResponse(response);
        assertFalse(timeZoneResponse.isSummerTimeMode());
        assertEquals(1, timeZoneResponse.getTimeZone());
    }

    @DisplayName("Test with wrong type")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParseWithWrongPort(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(2)));
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> new TimeZoneResponse(response));

        //then
        assertEquals("Can't convert string to boolean", e.getMessage());
    }

    @Test
    void testValidationFailed() {
        //when
        TimeZoneResponse timeZoneResponse = new TimeZoneResponse(true, 15);
        Set<ConstraintViolation<TimeZoneResponse>> constraintViolations = validator.validate(timeZoneResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<TimeZoneResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '15' should be from -12 to 12", result.get(0));
    }

    @Test
    void testValidationSuccess() {
        //when
        TimeZoneResponse timeZoneResponse = new TimeZoneResponse(true, 2);
        Set<ConstraintViolation<TimeZoneResponse>> constraintViolations = validator.validate(timeZoneResponse);

        //then
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(TimeZoneResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(TimeZoneResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(
                new ArrayList<>(Arrays.asList("<KPT>320$1$1$2</KPT>", "<KPT>320$1$0$1</KPT>", "<KPT>320$1$3$1</KPT>>"))));
    }
}