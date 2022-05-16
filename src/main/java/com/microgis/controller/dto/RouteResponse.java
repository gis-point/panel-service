package com.microgis.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteResponse {

    private String bus;

    private String routeName;

    private String arrivalMin;

    public RouteResponse(String routeName) {
        this.routeName = routeName;
    }

    public RouteResponse(String bus, String routeName) {
        this.bus = bus;
        this.routeName = routeName;
    }

}