package com.microgis.controller;

import com.microgis.controller.dto.RoutesModeResponse;
import com.microgis.controller.dto.panel.Command;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.response.AnswerSyncResponse;
import com.microgis.response.ComplexCommunicationParametersResponse;
import com.microgis.response.PanelDataResponse;
import com.microgis.response.Response;
import com.microgis.server.Server;
import com.microgis.util.ExceptionProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class PanelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Finds all panels which connected to the server
     *
     * @return 200 with body see {@link List<Panel>}
     * in case of error or no panel found returns {@link com.microgis.controller.dto.ErrorResponse}
     */
    @GetMapping("/info")
    public ResponseEntity<List<Panel>> getPanelById() {
        LOGGER.trace("Calling getPanelById");
        List<Panel> panels = server.getAllPanels();
        if (!CollectionUtils.isEmpty(panels)) {
            panels.stream()
                    .filter(panel -> !CollectionUtils.isEmpty(panel.getRouteResponses()))
                    .forEach(panel ->
                            panel.getRouteResponses()
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .forEach(routeResponse -> {
                                        if (routeResponse.getRouteName() != null) {
                                            routeResponse.setRouteName(routeResponse.getRouteName().replace("|", ""));
                                        }
                                    })
                    );
            return ResponseEntity.ok(panels);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Processing READ_PHYSICAL_INFO(303) command
     * Read physical info from panel by id
     *
     * @param id panel id
     * @return {@link PanelDataResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/info")
    public ResponseEntity<PanelDataResponse> readPanelInfo(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readPanelInfo with id-{}", id);
        PanelDataResponse panelDataResponse = null;
        try {
            this.server.executeCommand(id, Command.READ_PHYSICAL_INFO);
            panelDataResponse = server.findListByType(id, PanelDataResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return panelDataResponse != null ? ResponseEntity.ok(panelDataResponse) : ResponseEntity.badRequest().build();
    }

    /**
     * Processing SYNC_PACKAGE(20) command
     * Ping panel by id to check the panel on or off
     *
     * @param id panel id
     * @return 200 with empty body if connection exist,
     * and 400 in case if panel off or error occurred see{@link com.microgis.controller.dto.ErrorResponse}
     */
    @GetMapping("/{id}/ping")
    public ResponseEntity<Response> pingPanel(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling pingPanel with id-{}", id);
        AnswerSyncResponse response = null;
        try {
            this.server.executeCommand(id, Command.SYNC_PACKAGE);
            response = server.findListByType(id, AnswerSyncResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    /**
     * Processing SET_COMPLEX_COMMUNICATION_PARAMETERS(4) command
     * Set complex communication parameters for panel by id
     *
     * @param id                              panel id
     * @param communicationParametersResponse see {@link ComplexCommunicationParametersResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/complex/parameters")
    public ResponseEntity<?> setComplexCommunicationParameters(
            @NotNull @PathVariable("id") Integer id,
            @RequestBody @Valid ComplexCommunicationParametersResponse communicationParametersResponse) {
        LOGGER.info("Calling setComplexCommunicationParameters with id-{} and communicationParametersResponse-{}",
                id, communicationParametersResponse);
        try {
            String[] values = this.server.convertCustomClassToArray(communicationParametersResponse);
            this.server.executeCommand(id, Command.SET_COMPLEX_COMMUNICATION_PARAMETERS, values);
            return ResponseEntity.ok().build();
        } catch (IOException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * List of all panels with power mode
     */
    @GetMapping("/panelsList")
    public ResponseEntity<List<RoutesModeResponse>> getPanelsList() {
        LOGGER.trace("Calling getPanelsList");
        return ResponseEntity.ok(server.getModeResponses());
    }

}