package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.LogicalNumberResponse;
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
public class LogicalNumberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogicalNumberController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_LOGICAL_NUMBER(404) command
     * Set new logic number to panel by id
     *
     * @param id            panel id
     * @param logicalNumber new panel logic number
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/logicalNumber/{logicalNumber}")
    public ResponseEntity<?> setLogicalNumber(@NotNull @PathVariable("id") Integer id,
                                              @NotNull @PathVariable("logicalNumber") String logicalNumber) {
        LOGGER.info("Calling setLogicalNumber with id-{} and logicalNumber-{}", id, logicalNumber);
        try {
            this.server.executeCommand(id, Command.SET_LOGICAL_NUMBER, logicalNumber);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_LOGICAL_NUMBER(405) command
     * Read panel number
     *
     * @param id panel id
     * @return {@link LogicalNumberResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/logicalNumber")
    public ResponseEntity<LogicalNumberResponse> readLogicalNumber(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readLogicalNumber with id-{}", id);
        LogicalNumberResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_LOGICAL_NUMBER);
            response = server.findListByType(id, LogicalNumberResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}