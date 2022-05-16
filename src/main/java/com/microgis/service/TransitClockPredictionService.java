package com.microgis.service;

import com.microgis.configuration.AppProperties;
import com.microgis.controller.dto.prediction.Dest;
import com.microgis.controller.dto.prediction.Pred;
import com.microgis.controller.dto.prediction.Prediction;
import com.microgis.controller.dto.prediction.StopInfo;
import com.microgis.document.Panels;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransitClockPredictionService implements PredictionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransitClockPredictionService.class);

    private static final String URL = "rs=%s&numPreds=3&format=json";
    private static final String ADDITIONAL_URL = "/api/v1/key/f78a2e9a/agency/1/command/predictions?";
    private static final String ROUTE_NUMBER_44976 = "44976";
    private static final String ROUTE_NUMBER_44221 = "44221";

    private final RestTemplate restTemplate;

    private final AppProperties app;

    private final PanelGtfsService panelGtfsService;

    @Override
    public List<Prediction> getPredictions(int stopId) {
        String url = app.getPredictionServerBaseUrl() + ADDITIONAL_URL + String.format(URL, stopId);
        LOGGER.info("Formatted url for transit clock - {}", url);
        Panels panels = panelGtfsService.findByNumber(stopId);
        if (panels != null && !CollectionUtils.isEmpty(panels.getRoutes())) {
            final StringBuilder urlBuilder = formatUrl(panels, stopId);
            url = urlBuilder.toString();
        }
        LOGGER.debug("Execute API call: {}", url);
        return getForEntity(url, stopId);
    }

    /**
     * Format url for case with
     *
     * @param panels record from db
     * @param stopId stop number
     * @return created url
     */
    private StringBuilder formatUrl(Panels panels, int stopId) {
        final StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(app.getPredictionServerBaseUrl()).append(ADDITIONAL_URL);
        panels.getRoutes()
                .forEach(route -> urlBuilder
                        .append("rs=")
                        .append(route.getGtfs())
                        .append((char) 124)
                        .append(stopId)
                        .append("&")
                );
        urlBuilder.append("numPreds=3");
        return urlBuilder;
    }

    /**
     * Makes REST call, handle and sort response
     *
     * @param url base url
     * @return list of predictions
     */
    private List<Prediction> getForEntity(String url, int stopId) {
        List<Prediction> result = new ArrayList<>();
        StopInfo stopInfo = callPredictionService(url, stopId);
        if (stopInfo != null && stopInfo.getPredictions() != null) {
            result = stopInfo.getPredictions();
            result = result.stream()
                    .filter(prediction -> {
                        List<Dest> destList = prediction.getDest();
                        if (CollectionUtils.isEmpty(destList)) {
                            return false;
                        }
                        Dest dest = destList.get(0);
                        if (dest == null) {
                            return false;
                        }
                        List<Pred> predList = dest.getPred();
                        return predList != null && !predList.isEmpty();
                    })
                    .sorted((o1, o2) -> {
                        String routeShortName1 = o1.getRouteShortName();
                        String routeShortName2 = o2.getRouteShortName();
                        return routeShortName1.compareTo(routeShortName2);
                    })
                    .collect(Collectors.toList());
        }
        return fixWrongStop(result);
    }

    /**
     * Calls prediction service
     *
     * @param url    url to call
     * @param stopId stop number
     * @return service response
     */
    private StopInfo callPredictionService(String url, int stopId) {
        ResponseEntity<StopInfo> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, StopInfo.class);
        } catch (Exception e) {
            app.evictCache("gtfs", "findByNumber" + stopId);
            LOGGER.error("Error occurred when calling prediction service - {}", e.getMessage());
            Panels panels = panelGtfsService.findGtfs(stopId);
            if (panels != null) {
                try {
                    responseEntity = restTemplate.getForEntity(formatUrl(panels, stopId).toString(), StopInfo.class);
                } catch (Exception ex) {
                    LOGGER.error("Error occurred when calling prediction service with new parameters - {}", ex.getMessage());
                    return null;
                }
            } else {
                return null;
            }
        }
        LOGGER.debug("Response from prediction service code - {}", responseEntity.getStatusCodeValue());
        return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody() : null;
    }

    @SuppressWarnings("java:S3776")
    private List<Prediction> fixWrongStop(List<Prediction> result) {
        for (Prediction p : result) {
            if ("982".equals(p.getRouteId()) && ROUTE_NUMBER_44221.equals(p.getStopId()) && "0".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Аквапарк");
            }
            if ("972".equals(p.getRouteId()) && ROUTE_NUMBER_44221.equals(p.getStopId()) && "1".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Аквапарк");
            }
            if ("903".equals(p.getRouteId()) && ROUTE_NUMBER_44221.equals(p.getStopId()) && "1".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Вернадського");
            }
            if ("1628".equals(p.getRouteId()) && p.getStopId().equals(ROUTE_NUMBER_44976) && "1".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Автовокзал");
            }
            if ("977".equals(p.getRouteId()) && p.getStopId().equals(ROUTE_NUMBER_44976) && "1".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Червоної Калини");
            }
            if ("1594".equals(p.getRouteId()) && p.getStopId().equals(ROUTE_NUMBER_44976) && "1".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Центр здоров'я");
            }
            if ("902".equals(p.getRouteId()) && "44252".equals(p.getStopId()) && "1".equals(p.getDest().get(0).getDir())) {
                p.getDest().get(0).setHeadsign("Миколайчука");
            }
        }
        return result;
    }
}