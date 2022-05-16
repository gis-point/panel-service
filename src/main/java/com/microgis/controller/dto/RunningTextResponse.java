package com.microgis.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunningTextResponse {

    /**
     * Number of route
     */
    private String route;

    /**
     * The text which will be set into the panel
     */
    @NotNull
    @Size(max = 96, message = "The value '${validatedValue}' should be no longer than 96 symbols")
    private String text;

    private boolean addToExistingMode = false;

    private Date expirationTime;

}