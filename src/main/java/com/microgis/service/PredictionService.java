package com.microgis.service;

import com.microgis.controller.dto.prediction.Prediction;

import java.util.List;

public interface PredictionService {

    List<Prediction> getPredictions(int stopId);

}