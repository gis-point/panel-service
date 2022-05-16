package com.microgis.controller.dto.prediction;

import lombok.Data;

import java.util.List;

@Data
public class Dest {

    private String dir;

    private String headsign;

    private List<Pred> pred = null;

}