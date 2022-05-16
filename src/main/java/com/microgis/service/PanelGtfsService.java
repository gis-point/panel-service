package com.microgis.service;

import com.microgis.configuration.AppProperties;
import com.microgis.controller.dto.GtfsResponse;
import com.microgis.document.Panels;
import com.microgis.document.repository.PanelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"gtfs"})
public class PanelGtfsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelGtfsService.class);

    private static final String URL = "/api/v1/key/f78a2e9a/agency/1/command/routes?format=json";

    private final PanelRepository panelRepository;

    private final RestTemplate restTemplate;

    private final AppProperties appProperties;

    /**
     * Find out and replace wrong gtfs
     *
     * @param stopId stop number
     * @return panel with correct gtfs
     */
    public Panels findGtfs(int stopId) {
        LOGGER.info("Finding gtfs for stopId - {}", stopId);
        Panels panels = findByNumber(stopId);
        GtfsResponse gtfsResponse = callGtfsService();
        if (gtfsResponse != null && panels != null) {
            gtfsResponse.getRoutes()
                    .forEach(routesResponse ->
                            panels.getRoutes()
                                    .stream()
                                    .filter(id -> id.getRouteName().equals(routesResponse.getShortName()))
                                    .forEach(route -> route.setGtfs(Integer.parseInt(routesResponse.getId())))
                    );
            LOGGER.info("Saving panel to db - {}", panels);
            panelRepository.save(panels);
        }
        return panels;
    }

    /**
     * Calls transitclock server
     *
     * @return response with correct gtfs values
     */
    private GtfsResponse callGtfsService() {
        String url = appProperties.getPredictionServerBaseUrl() + URL;
        LOGGER.info("Calling gtfs service with URL - {}", url);
        ResponseEntity<GtfsResponse> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(url, GtfsResponse.class);
        } catch (Exception e) {
            LOGGER.error("Couldn't get response from gtfs service - {}", e.getMessage());
            return null;
        }
        return responseEntity.getStatusCode() == HttpStatus.OK ? responseEntity.getBody() : null;
    }

    /**
     * Find out panels info by stopId from db
     *
     * @param stopId stop number
     * @return panels info
     */
    @Cacheable(key = "#root.methodName + #stopId")
    public Panels findByNumber(int stopId) {
        Optional<Panels> panels = panelRepository.findByNumber(stopId);
        return panels.isPresent() && !CollectionUtils.isEmpty(panels.get().getRoutes()) ? panels.get() : null;
    }
}