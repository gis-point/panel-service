package com.microgis.controller.dto.prediction;

import lombok.Data;

import java.util.List;

@Data
public class Prediction {

    private String routeShortName;

    private String routeName;

    private String routeId;

    private String stopId;

    private String stopName;

    private Integer stopCode;

    private List<Dest> dest = null;

}