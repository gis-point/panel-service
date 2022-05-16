package com.microgis.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class GtfsResponse {

    private String agency;

    private List<RoutesResponse> routes;

}