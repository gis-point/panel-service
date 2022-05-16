package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.ServerSettingsResponse;
import com.microgis.server.Server;
import com.microgis.util.ExceptionProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class ServerSettingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSettingsController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SERVER_SETTINGS(52) command
     * Change ip and port for panel by id
     *
     * @param id   panel id
     * @param ip   new ip for panel
     * @param port new port for panel
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/{ip}/{port}/server/settings")
    public ResponseEntity<?> serverSettings(@NotNull @PathVariable int id, @NotNull @PathVariable String ip,
                                            @NotNull @PathVariable String port) {
        LOGGER.info("Calling serverSettings with id-{}, ip-{} and port-{}", id, ip, port);
        try {
            this.server.executeCommand(id, Command.SERVER_SETTINGS, ip, port);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing GET_SERVER_SETTINGS(53) command
     * Read server settings from panel by id
     *
     * @param id panel id
     * @return {@link ServerSettingsResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/server/settings")
    public ResponseEntity<ServerSettingsResponse> readServerSettings(@NotNull @PathVariable int id) {
        LOGGER.info("Calling readServerSettings with id-{}", id);
        ServerSettingsResponse response = null;
        try {
            this.server.executeCommand(id, Command.GET_SERVER_SETTINGS);
            response = server.findListByType(id, ServerSettingsResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}