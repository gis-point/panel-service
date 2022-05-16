package com.microgis.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutesModeResponse {

    private String stopName;

    private int number;

    private String mode = "0";

    private String phoneNumber;

    private String lastConnection;
}
