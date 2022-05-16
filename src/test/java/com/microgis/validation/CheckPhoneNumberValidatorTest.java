package com.microgis.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CheckPhoneNumberValidatorTest {

    @InjectMocks
    private CheckPhoneNumberValidator checkPhoneNumberValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    public void testIsValidEmpty() {
        //when then
        assertTrue(checkPhoneNumberValidator.isValid("", constraintValidatorContext));
    }

    @Test
    public void testIsValidFalse() {
        //when then
        assertFalse(checkPhoneNumberValidator.isValid("+38067453423", constraintValidatorContext));
    }

    @Test
    public void testIsValidTrue() {
        //when then
        assertTrue(checkPhoneNumberValidator.isValid("+380674534232", constraintValidatorContext));
    }
}