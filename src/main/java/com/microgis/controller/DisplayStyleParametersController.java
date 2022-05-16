package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.DisplayStyleParametersResponse;
import com.microgis.server.Server;
import com.microgis.util.ExceptionProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class DisplayStyleParametersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayStyleParametersController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS(306) command
     * Set display style parameters for panel by id
     *
     * @param id                             panel id
     * @param displayStyleParametersResponse see {@link DisplayStyleParametersResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/style")
    public ResponseEntity<?> setDisplayStyleParameters(
            @NotNull @PathVariable("id") Integer id,
            @RequestBody @Valid DisplayStyleParametersResponse displayStyleParametersResponse) {
        LOGGER.info("Calling setDisplayStyleParameters with id-{} and displayStyleParametersResponse-{}", id,
                displayStyleParametersResponse);
        try {
            String[] values = server.convertCustomClassToArray(displayStyleParametersResponse);
            this.server.executeCommand(id, Command.SET_DISPLAY_STYLE_PARAMETERS, values);
            return ResponseEntity.ok().build();
        } catch (IOException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_DISPLAY_STYLE_PARAMETERS(307) command
     * Read style configuration from panel by id
     *
     * @param id panel id
     * @return {@link DisplayStyleParametersResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/style")
    public ResponseEntity<DisplayStyleParametersResponse> readDisplayStyleParameters(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readDisplayStyleParameters with id-{}", id);
        DisplayStyleParametersResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_DISPLAY_STYLE_PARAMETERS);
            response = server.findListByType(id, DisplayStyleParametersResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}