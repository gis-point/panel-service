package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.GprsResponse;
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
public class GprsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GprsController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_GPRS(50) command
     * Set new gprs settings for panel by id
     *
     * @param id           panel id
     * @param gprsResponse see {@link GprsResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/gprs")
    public ResponseEntity<?> setGprsCoordinates(@NotNull @PathVariable("id") Integer id,
                                                @RequestBody @Valid GprsResponse gprsResponse) {
        LOGGER.info("Calling setGprsCoordinates with id-{} and gprsResponse-{}", id, gprsResponse);
        try {
            this.server.executeCommand(id, Command.SET_GPRS, gprsResponse.getAddress(), gprsResponse.getLogin(),
                    gprsResponse.getPassword());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_GPRS(51) command
     * Read gprs info from panel by id
     *
     * @param id panel id
     * @return 200 with body {@link GprsResponse class }if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/gprs")
    public ResponseEntity<GprsResponse> getGprsCoordinates(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling getGprsCoordinates with id-{}", id);
        GprsResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_GPRS);
            response = server.findListByType(id, GprsResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}