package com.microgis.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PanelResponse {

    private long id;

    @NotNull
    private String stopName;

    @NotNull
    private int number;

    @NotNull
    private String phoneNumber;

}