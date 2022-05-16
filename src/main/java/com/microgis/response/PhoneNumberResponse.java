package com.microgis.response;

import com.microgis.validation.CheckPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PhoneNumberResponse extends Response {

    /**
     * Slot in panel should be from 1 to 9
     */
    @Min(value = 1, message = "The value '${validatedValue}' should be from 1 to 9")
    @Max(value = 9, message = "The value '${validatedValue}' should be from 1 to 9")
    private int number;

    /**
     * Phone number for contact with panel
     */
    @CheckPhoneNumber
    private String phoneNumber;

}