package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.TimeZoneResponse;
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
public class TimeZoneController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeZoneController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_TIME_ZONE(320) command
     * Set time zone and summer mode for panel by id
     *
     * @param id               panel id
     * @param timeZoneResponse see {@link TimeZoneResponse}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/timeZone")
    public ResponseEntity<?> setTimeZone(@NotNull @PathVariable("id") Integer id,
                                         @Valid @RequestBody TimeZoneResponse timeZoneResponse) {
        LOGGER.info("Calling setTimeZone with id-{} and timeZoneResponse-{}", id, timeZoneResponse);
        try {
            this.server.executeCommand(id, Command.SET_TIME_ZONE, String.valueOf(timeZoneResponse.isSummerTimeMode()),
                    String.valueOf(timeZoneResponse.getTimeZone()));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_TIME_ZONE(321) command
     * Read time zone and summer time mode from panel by id
     *
     * @param id panel id
     * @return {@link TimeZoneResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/timeZone")
    public ResponseEntity<TimeZoneResponse> readTimeZone(@NotNull @PathVariable("id") Integer id) {
        LOGGER.info("Calling readTimeZone with id-{}", id);
        TimeZoneResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_TIME_ZONE);
            response = server.findListByType(id, TimeZoneResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}
