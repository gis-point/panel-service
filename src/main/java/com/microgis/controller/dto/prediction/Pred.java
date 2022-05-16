package com.microgis.controller.dto.prediction;

import lombok.Data;

@Data
public class Pred {

    private Integer time;

    private Integer sec;

    private Integer min;

    private String trip;

    private String blockId;

    private String tripPattern;

    private String vehicle;

    private Boolean lateAndSubsequentTripSoMarkAsUncertain;

    private String isDeparture;

    private String affectedByLayover;

    private Boolean notYetDeparted;
}
