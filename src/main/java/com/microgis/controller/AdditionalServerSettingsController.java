package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.AdditionalServerSettingsResponse;
import com.microgis.response.ServerSettingsResponse;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class AdditionalServerSettingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdditionalServerSettingsController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_ADDITIONAL_SERVER(400)) command
     * Set additional server ip and port for panel by id
     *
     * @param id   panel id
     * @param ip   additional ip for panel
     * @param port additional port for panel
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/{ip}/{port}/additionalServer")
    public ResponseEntity<?> setAdditionalServer(@NotNull @PathVariable int id, @NotNull @PathVariable String ip,
                                                 @NotNull @Valid @Min(1) @Max(65535) @PathVariable Integer port) {
        LOGGER.info("Calling setAdditionalServer with id-{}, ip-{} and port-{}", id, ip, port);
        try {
            this.server.executeCommand(id, Command.SET_ADDITIONAL_SERVER, ip, String.valueOf(port));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_ADDITIONAL_SERVER(401) command
     * Read server settings from panel by id
     *
     * @param id panel id
     * @return {@link ServerSettingsResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/additionalServer")
    public ResponseEntity<AdditionalServerSettingsResponse> readAdditionalServer(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readAdditionalServer with id-{}", id);
        AdditionalServerSettingsResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_ADDITIONAL_SERVER);
            response = server.findListByType(id, AdditionalServerSettingsResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }

    /**
     * Processing SET_ADDITIONAL_SERVER(400) command
     * Discards additional server
     *
     * @param id panel id
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/additionalServer/discard")
    public ResponseEntity<?> discardAdditionalServer(@NotNull @PathVariable int id) {
        LOGGER.info("Calling discardAdditionalServer with id-{}", id);
        try {
            this.server.executeCommand(id, Command.SET_ADDITIONAL_SERVER);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing SET_ADDITIONAL_SERVER_TIMEOUT(402) command
     * Set timeout for connection
     *
     * @param id panel id
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/time/{time}/additionalServer")
    public ResponseEntity<?> setTimeoutAdditionalServer(@NotNull @PathVariable int id, @NotNull @PathVariable int time) {
        LOGGER.info("Calling setTimeoutAdditionalServer with id-{} and time-{}", id, time);
        try {
            this.server.executeCommand(id, Command.SET_ADDITIONAL_SERVER_TIMEOUT, String.valueOf(time));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }
}