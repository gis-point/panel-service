package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.StopNameResponse;
import com.microgis.server.Server;
import com.microgis.util.ExceptionProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class StopNameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopNameController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_STOP_NAME(60) command
     * Set stop name by id
     *
     * @param id       panel id
     * @param stopName stop name
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/stopName/{stopName}")
    public ResponseEntity<?> setStopName(@NotNull @PathVariable("id") Integer id,
                                         @NotNull @Valid @Size(max = 96) @PathVariable("stopName") String stopName) {
        LOGGER.info("Calling setStopName with id-{} and stop name-{}", id, stopName);
        try {
            this.server.executeCommand(id, Command.SET_STOP_NAME, stopName);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing SET_STOP_NAME(60) command
     * Reset stop name by id
     *
     * @param id panel id
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/stopName/reset")
    public ResponseEntity<?> resetStopName(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling resetStopName with id-{}", id);
        try {
            this.server.executeCommand(id, Command.SET_STOP_NAME);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing GET_STOP_NAME(61) command
     * Read stop name from panel by id
     *
     * @param id panel id
     * @return {@link StopNameResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/stopName")
    public ResponseEntity<StopNameResponse> readStopName(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readStopName with id-{}", id);
        StopNameResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_STOP_NAME);
            response = server.findListByType(id, StopNameResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}
