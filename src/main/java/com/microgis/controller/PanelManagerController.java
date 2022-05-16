package com.microgis.controller;

import com.microgis.controller.dto.PanelResponse;
import com.microgis.service.PanelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
public class PanelManagerController {

    private final PanelService panelService;

    @GetMapping("/allPanel")
    public ResponseEntity<List<PanelResponse>> getAllPanels(@RequestBody PanelResponse panelResponse) {
        List<PanelResponse> panelsList = panelService.getAll();
        return ResponseEntity.ok(panelsList);
    }

    @PostMapping("/addPanel")
    public ResponseEntity<Long> addPanel(@RequestBody PanelResponse panelResponse) {
        long panelId = panelService.insertPanel(panelResponse);
        return ResponseEntity.ok(panelId);
    }

    @PutMapping("/updatePanel")
    public ResponseEntity<Long> updatePanel(@RequestBody PanelResponse panelResponse) {
        long panelId = panelService.updatePanel(panelResponse);
        return ResponseEntity.ok(panelId);
    }

    @DeleteMapping("/deletePanel/{id}")
    public ResponseEntity<Object> deletePanel(@PathVariable("id") long id) {
        panelService.delete(id);
        return ResponseEntity.ok().build();
    }

}