package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.DisplayStyleScheduleParametersResponse;
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
public class DisplayStyleScheduleParametersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayStyleScheduleParametersController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS(304) command
     * Set display style schedule parameters for panel by id
     *
     * @param id                                     panel id
     * @param displayStyleScheduleParametersResponse see {@link DisplayStyleScheduleParametersResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/schedule/style")
    public ResponseEntity<?> setDisplayStyleScheduleParameters(
            @NotNull @PathVariable("id") Integer id,
            @RequestBody @Valid DisplayStyleScheduleParametersResponse displayStyleScheduleParametersResponse) {
        LOGGER.info("Calling setDisplayStyleScheduleParameters with id-{} and displayStyleScheduleParametersResponse-{}",
                id, displayStyleScheduleParametersResponse);
        try {
            String[] values = this.server.convertCustomClassToArray(displayStyleScheduleParametersResponse);
            this.server.executeCommand(id, Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS, values);
            return ResponseEntity.ok().build();
        } catch (IOException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS(305) command
     * Read display style schedule parameters from panel by id
     *
     * @param id panel id
     * @return 200 with body {@link DisplayStyleScheduleParametersResponse class }if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/schedule/style")
    public ResponseEntity<DisplayStyleScheduleParametersResponse> getDisplayStyleScheduleParameters(
            @NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling getDisplayStyleScheduleParameters with id-{}", id);
        DisplayStyleScheduleParametersResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS);
            response = server.findListByType(id, DisplayStyleScheduleParametersResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}