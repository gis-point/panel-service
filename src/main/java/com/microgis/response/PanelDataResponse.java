package com.microgis.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PanelDataResponse extends Response {

    /**
     * Скорость перемещения бегущей строки
     */
    private int textSpeed;

    /**
     * Максимальный порог яркости свечения табло
     */
    private int maxBrightness;

    /**
     * Минимальный порог яркости свечения табло
     */
    private int minBrightness;

    /**
     * Признак наличия автоматического управления яркостью
     */
    private boolean automaticMode;

    /**
     * Текущая яркость свечения табло
     */
    private int currentBrightness;

    /**
     * Текущая освещенность табло
     */
    private int currentIllumination;

    /**
     * Признак наличия датчика температуры
     */
    private boolean temperatureMode;

    /**
     * Значение измеренной температуры
     */
    private double currentTemperature;

    public PanelDataResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.textSpeed = Integer.parseInt(indexInBound(data, 2));
        this.maxBrightness = Integer.parseInt(indexInBound(data, 3));
        this.minBrightness = Integer.parseInt(indexInBound(data, 4));
        this.automaticMode = convertStringToBoolean(indexInBound(data, 5));
        this.currentBrightness = Integer.parseInt(indexInBound(data, 6));
        this.currentIllumination = Integer.parseInt(indexInBound(data, 7));
        this.temperatureMode = convertStringToBoolean(indexInBound(data, 8));
        this.currentTemperature = Double.parseDouble(indexInBound(data, 9));
    }
}
