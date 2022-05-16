package com.microgis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicalInfoResponse {

    /**
     * Running text speed in a row
     */
    private int textSpeed;

    /**
     * Max panel brightness should be from 1 to 255
     */
    @Min(value = 1, message = "The value '${validatedValue}' should be from 1 to 255")
    @Max(value = 255, message = "The value '${validatedValue}' should be from 1 to 255")
    private int maxBrightness;

    /**
     * Min panel brightness should be from 1 to 255
     */
    @Min(value = 1, message = "The value '${validatedValue}' should be from 1 to 255")
    @Max(value = 255, message = "The value '${validatedValue}' should be from 1 to 255")
    private int minBrightness;

}