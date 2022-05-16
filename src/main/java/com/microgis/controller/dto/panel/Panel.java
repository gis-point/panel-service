package com.microgis.controller.dto.panel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.microgis.controller.dto.RouteResponse;
import com.microgis.controller.dto.RunningTextResponse;
import com.microgis.response.PanelDataResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Panel {

    private String stopName;

    private boolean identified;

    private int physicalNumber;

    private int logicalNumber;

    private String runningLineText;

    private int viewRouteCounts;

    private int stopId;

    private boolean isReloadMode;

    private String port;

    private String ip;

    private String time;

    private PanelDataResponse panelDataResponse;

    /**
     * All buses for current panel
     */
    private Set<String> routes = new HashSet<>();

    /**
     * Switch static text mode
     */
    private volatile boolean staticTextMode = false;

    /**
     * Switch running text mode
     */
    private volatile boolean runningTextMode = false;

    /**
     * Created to check response from current panel
     */
    private volatile boolean timerMode = true;

    /**
     * Map to store bus number and running text
     */
    private List<RunningTextResponse> runningTextForRoute = new CopyOnWriteArrayList<>();

    /**
     * List of buses divided to bus, route and arrival
     */
    private List<RouteResponse> routeResponses = new CopyOnWriteArrayList<>();

    /**
     * Static text outputted in panel
     */
    private volatile String staticText;

    public void incrementViewRouteCounts() {
        viewRouteCounts++;
    }

    public void resetViewRouteCounts() {
        this.viewRouteCounts = 0;
    }

    @JsonIgnore
    public boolean isRunningTextMode() {
        return runningTextMode;
    }

    @JsonIgnore
    public String getRunningLineText() {
        return runningLineText;
    }

    @JsonIgnore
    public List<RunningTextResponse> getRunningTextForRoute() {
        return runningTextForRoute;
    }

    @JsonIgnore
    public boolean isTimerMode() {
        return timerMode;
    }

    @JsonIgnore
    public boolean isReloadMode() {
        return isReloadMode;
    }

    @JsonIgnore
    public boolean isIdentified() {
        return identified;
    }

    @JsonIgnore
    public int getPhysicalNumber() {
        return physicalNumber;
    }

}