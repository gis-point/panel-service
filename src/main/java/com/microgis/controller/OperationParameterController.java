package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.OperationParametersResponse;
import com.microgis.server.Server;
import com.microgis.util.ExceptionProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class OperationParameterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationParameterController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_OPERATION_PARAMETERS(310) command
     * Set operation parameters to panel by id
     *
     * @param id                          panel id
     * @param operationParametersResponse see {@link OperationParametersResponse}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/operationParameters")
    public ResponseEntity<?> setOperationParameters(
            @NotNull @PathVariable("id") Integer id,
            @NotNull @Valid @RequestBody OperationParametersResponse operationParametersResponse) {
        LOGGER.info("Calling setOperationParameters with id-{} and operationParametersResponse-{}", id,
                operationParametersResponse);
        try {
            String[] values = this.server.convertCustomClassToArray(operationParametersResponse);
            this.server.executeCommand(id, Command.SET_OPERATION_PARAMETERS, values);
            return ResponseEntity.ok().build();
        } catch (IOException | IllegalAccessException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_OPERATION_PARAMETERS(311) command
     * Read panel operation parameters
     *
     * @param id panel id
     * @return {@link OperationParametersResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/operationParameters")
    public ResponseEntity<OperationParametersResponse> readOperationParameters(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readOperationParameters with id-{}", id);
        OperationParametersResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_OPERATION_PARAMETERS);
            response = server.findListByType(id, OperationParametersResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}