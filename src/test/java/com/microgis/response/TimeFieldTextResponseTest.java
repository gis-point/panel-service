package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TimeFieldTextResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_TEXT_FOR_TIME_FIELD, response.getCommand());
        TimeFieldTextResponse timeFieldTextResponse = new TimeFieldTextResponse(response);
        assertEquals(0, timeFieldTextResponse.getNumber());
        assertEquals("Прибуває Is Arriving", timeFieldTextResponse.getText());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_TEXT_FOR_TIME_FIELD, response.getCommand());
        timeFieldTextResponse = new TimeFieldTextResponse(response);
        assertEquals(1, timeFieldTextResponse.getNumber());
        assertEquals("Затримується Delay", timeFieldTextResponse.getText());
    }

    @DisplayName("Test with wrong type")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParseWithWrongPort(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(2)));
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> new TimeFieldTextResponse(response));

        //then
        assertEquals("For input string: \"any\"", e.getMessage());
    }

    @RepeatedTest(3)
    void testValidationFailed() {
        //when
        TimeFieldTextResponse timeFieldTextResponse = new TimeFieldTextResponse(50,
                "Текст надписи может содержать одновременно тексты на разных"
                        + " языках. Эти тексты должны отделяться символом табуляции. Текст надписи может содержать одновременно тексты на разных"
                        + " языках. Эти тексты должны отделяться символом табуляции. Текст надписи может содержать одновременно тексты на разных"
                        + " языках. Эти тексты должны отделяться символом табуляции");
        Set<ConstraintViolation<TimeFieldTextResponse>> constraintViolations = validator.validate(timeFieldTextResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<TimeFieldTextResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '50' should be from 0 to 2", result.get(0));
        assertEquals(
                "The value 'Текст надписи может содержать одновременно тексты на разных языках. Эти тексты должны отделяться символом табуляции. Текст надписи может содержать одновременно тексты на разных языках. Эти тексты должны отделяться символом табуляции. Текст надписи может содержать одновременно тексты на разных языках. Эти тексты должны отделяться символом табуляции' should be no longer than 48 symbols",
                result.get(1));
    }

    @Test
    void testValidationSuccess() {
        //when
        TimeFieldTextResponse timeFieldTextResponse = new TimeFieldTextResponse(1, "any");
        Set<ConstraintViolation<TimeFieldTextResponse>> constraintViolations = validator.validate(timeFieldTextResponse);

        //then
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(TimeFieldTextResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(TimeFieldTextResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(new ArrayList<>(
                Arrays.asList("<KPT>14$353$0$Прибуває Is Arriving</KPT>", "<KPT>14$353$1$Затримується Delay</KPT>",
                        "<KPT>14$353$any$Затримується Delay</KPT>>"))));
    }
}