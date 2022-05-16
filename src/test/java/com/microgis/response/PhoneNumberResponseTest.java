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

class PhoneNumberResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @RepeatedTest(3)
    void testValidationFailed() {
        //when
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(0, "345");
        Set<ConstraintViolation<PhoneNumberResponse>> constraintViolations = validator.validate(phoneNumberResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<PhoneNumberResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '345' should be in like +380671234567", result.get(1));
        assertEquals("The value '0' should be from 1 to 9", result.get(0));
    }

    @Test
    void testValidationSuccess() {
        //when
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380671234567");
        Set<ConstraintViolation<PhoneNumberResponse>> constraintViolations = validator.validate(phoneNumberResponse);

        //then
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(PhoneNumberResponse.class)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(PhoneNumberResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}