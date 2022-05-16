package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

class ComplexCommunicationParametersResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @RepeatedTest(2)
    void testValidationFailed() {
        //when
        ComplexCommunicationParametersResponse parametersResponse = new ComplexCommunicationParametersResponse(
                "The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols",
                "The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols",
                "The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols",
                "The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols",
                0);
        Set<ConstraintViolation<ComplexCommunicationParametersResponse>> constraintViolations = validator
                .validate(parametersResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<ComplexCommunicationParametersResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '0' should be from 1 to 65535", result.get(0));
        assertEquals(
                "The value 'The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols' should be no longer than 63 symbols",
                result.get(1));
        assertEquals(
                "The value 'The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols' should be no longer than 63 symbols",
                result.get(2));
        assertEquals(
                "The value 'The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols' should be no longer than 20 symbols",
                result.get(3));
        assertEquals(
                "The value 'The value '${validatedValue}' should be no longer than 63 symbols The value '${validatedValue}' should be no longer than 63 symbols' should be no longer than 20 symbols",
                result.get(4));
    }

    @Test
    void testValidationSuccess() {
        //when
        ComplexCommunicationParametersResponse parametersResponse = new ComplexCommunicationParametersResponse("address",
                "login", "password", "serverAddress", 1);
        Set<ConstraintViolation<ComplexCommunicationParametersResponse>> constraintViolations = validator
                .validate(parametersResponse);

        //then
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(ComplexCommunicationParametersResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ComplexCommunicationParametersResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}