package com.microgis.controller.dto.prediction;

import lombok.Data;

import java.util.List;

@Data
public class StopInfo {

    private List<Prediction> predictions = null;

}