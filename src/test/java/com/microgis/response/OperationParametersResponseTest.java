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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OperationParametersResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("Test with correct result")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParse(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(0)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_OPERATION_PARAMETERS, response.getCommand());
        OperationParametersResponse operationParametersResponse = new OperationParametersResponse(response);
        assertEquals(0, operationParametersResponse.getSortMode().intValue());
        assertEquals(10, operationParametersResponse.getServerTimeout().intValue());
        assertEquals(24, operationParametersResponse.getKeepRouteRecordTime().intValue());
        assertEquals(19, operationParametersResponse.getStartEveningTime().intValue());
        assertEquals(4, operationParametersResponse.getFinishEveningTime().intValue());
        assertEquals(300, operationParametersResponse.getAdditionalServerInterval().intValue());
        assertEquals(2, operationParametersResponse.getAdditionalServerConnectionInterval().intValue());

        //when
        response = new Response(new StringBuilder(packet.get(1)));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_OPERATION_PARAMETERS, response.getCommand());
        operationParametersResponse = new OperationParametersResponse(response);
        assertEquals(0, operationParametersResponse.getSortMode().intValue());
        assertEquals(10, operationParametersResponse.getServerTimeout().intValue());
        assertEquals(24, operationParametersResponse.getKeepRouteRecordTime().intValue());
        assertEquals(44, operationParametersResponse.getStartEveningTime().intValue());
        assertEquals(4, operationParametersResponse.getFinishEveningTime().intValue());
        assertEquals(1, operationParametersResponse.getAdditionalServerInterval().intValue());
        assertEquals(20, operationParametersResponse.getAdditionalServerConnectionInterval().intValue());
    }

    @DisplayName("Test with wrong type")
    @ParameterizedTest
    @MethodSource("packetProvider")
    void testParseWithWrongPort(List<String> packet) {
        //when
        Response response = new Response(new StringBuilder(packet.get(2)));
        NumberFormatException e = assertThrows(NumberFormatException.class, () -> new OperationParametersResponse(response));

        //then
        assertEquals("For input string: \"mode\"", e.getMessage());
    }

    @Test
    void testValidationFailedMax() {
        //when
        OperationParametersResponse operationParametersResponse = new OperationParametersResponse(3, 10, 24, 44, 4, 1, 20);
        Set<ConstraintViolation<OperationParametersResponse>> constraintViolations = validator
                .validate(operationParametersResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<OperationParametersResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '3' should be from 0 to 2", result.get(0));
    }

    @Test
    void testValidationFailedMin() {
        //when
        OperationParametersResponse operationParametersResponse = new OperationParametersResponse(-3, 10, 24, 44, 4, 1, 20);
        Set<ConstraintViolation<OperationParametersResponse>> constraintViolations = validator
                .validate(operationParametersResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<OperationParametersResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '-3' should be from 0 to 2", result.get(0));
    }

    @Test
    void testValidationSuccess() {
        //when
        OperationParametersResponse operationParametersResponse = new OperationParametersResponse(0, 10, 24, 44, 4, 1, 20);
        Set<ConstraintViolation<OperationParametersResponse>> constraintViolations = validator
                .validate(operationParametersResponse);

        //then
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(OperationParametersResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(OperationParametersResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }

    private static Stream<Arguments> packetProvider() {
        return Stream.of(arguments(new ArrayList<>(
                Arrays.asList("<KPT>310$1$0$10$24$19$4$300$2</KPT>", "<KPT>310$1$0$10$24$44$4$1$20</KPT>",
                        "<KPT>310$1$mode$10$24$44$4$1$20</KPT>"))));
    }
}