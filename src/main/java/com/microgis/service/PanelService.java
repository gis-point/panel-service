package com.microgis.service;

import com.microgis.controller.dto.PanelResponse;
import com.microgis.document.Panels;
import com.microgis.document.repository.PanelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PanelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelService.class);

    private final PanelRepository panelRepository;

    public List<PanelResponse> getAll() {
        return panelRepository.findAll()
                .stream()
                .map(panels -> {
                    PanelResponse panelResponse = new PanelResponse();
                    panelResponse.setId(panels.getId());
                    panelResponse.setNumber(panels.getNumber());
                    panelResponse.setStopName(panels.getStopName());
                    panelResponse.setPhoneNumber(panels.getPhoneNumber());
                    return panelResponse;
                })
                .collect(Collectors.toList());
    }

    public long insertPanel(PanelResponse panelResponse) {
        Panels panels = new Panels();
        long id = getId() + 1;
        panelResponse.setId(id);
        setPanelValues(panelResponse, panels);
        panelRepository.save(panels);
        LOGGER.info("Added panel with id - {}", id);
        return id;
    }

    public long updatePanel(PanelResponse panelResponse) {
        Optional<Panels> panel = panelRepository.findById(panelResponse.getId());
        if (panel.isPresent()) {
            Panels panels = panel.get();
            setPanelValues(panelResponse, panels);
            panels = panelRepository.save(panels);
            LOGGER.info("Updated panel with id - {}", panels.getId());
            return panels.getId();
        }
        LOGGER.info("Cannot update panel with id - {}", panelResponse.getId());
        return 0;
    }

    public void delete(long id) {
        panelRepository.deleteById(id);
        LOGGER.info("Deleted panel with id - {}", id);
    }

    private long getId() {
        return panelRepository.findAll().stream()
                .mapToLong(Panels::getId)
                .max()
                .orElse(1);
    }

    private void setPanelValues(PanelResponse panelResponse, Panels panels) {
        panels.setId(panelResponse.getId());
        panels.setNumber(panelResponse.getNumber());
        panels.setStopName(panelResponse.getStopName());
        panels.setPhoneNumber(panelResponse.getPhoneNumber());
        panels.setRoutes(new ArrayList<>());
    }

}