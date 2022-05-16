package com.microgis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TimeZoneResponse extends Response {

    /**
     * Summer time 0 - turn off, 1 - turn on
     */
    private boolean summerTimeMode;

    /**
     * Time zone should be from -12 to 12
     */
    @Min(value = -12, message = "The value '${validatedValue}' should be from -12 to 12")
    @Max(value = 12, message = "The value '${validatedValue}' should be from -12 to 12")
    private int timeZone;

    public TimeZoneResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.summerTimeMode = convertStringToBoolean(indexInBound(data, 2));
        this.timeZone = Integer.parseInt(indexInBound(data, 3));
    }
}
