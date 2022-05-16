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

class PhysicalInfoResponseTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @RepeatedTest(3)
    void testValidationFailed() {
        //when
        PhysicalInfoResponse physicalInfoResponse = new PhysicalInfoResponse(50, 0, -44);
        Set<ConstraintViolation<PhysicalInfoResponse>> constraintViolations = validator.validate(physicalInfoResponse);
        List<String> result = new ArrayList<>();
        for (ConstraintViolation<PhysicalInfoResponse> value : constraintViolations) {
            result.add(value.getMessage());
        }
        result = result.stream().sorted().collect(Collectors.toList());

        //then
        assertEquals("The value '-44' should be from 1 to 255", result.get(0));
        assertEquals("The value '0' should be from 1 to 255", result.get(1));
    }

    @Test
    void testValidationSuccess() {
        //when
        PhysicalInfoResponse physicalInfoResponse = new PhysicalInfoResponse(50, 1, 1);
        Set<ConstraintViolation<PhysicalInfoResponse>> constraintViolations = validator.validate(physicalInfoResponse);

        //then
        assertEquals(0, constraintViolations.size());
    }

    @Test
    void testEquals() {
        EqualsVerifier.forClass(PhysicalInfoResponse.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(PhysicalInfoResponse.class).verify();
    }
}