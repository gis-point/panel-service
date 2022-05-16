package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.response.TimeFieldTextResponse;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class TimeFieldTextController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeFieldTextController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_TEXT_FOR_TIME_FIELD(14) command
     * Set text into time field for panel by id
     *
     * @param id                    panel id
     * @param timeFieldTextResponse see {@link TimeZoneResponse}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/timeField")
    public ResponseEntity<?> setTextTimeField(@NotNull @PathVariable("id") Integer id,
                                              @Valid @RequestBody TimeFieldTextResponse timeFieldTextResponse) {
        LOGGER.info("Calling setTextTimeField with id-{} and timeFieldTextResponse-{}", id, timeFieldTextResponse);
        try {
            this.server.executeCommand(id, Command.SET_TEXT_FOR_TIME_FIELD, String.valueOf(timeFieldTextResponse.getNumber()),
                    String.valueOf(timeFieldTextResponse.getText()));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing READ_TEXT_FOR_TIME_FIELD(15) command
     * Read text from field time from panel by id and slot number
     *
     * @param id     panel id
     * @param number value between 0 to 2 (text slot)
     * @return {@link TimeFieldTextResponse class if response code 200}
     * and {@link com.microgis.controller.dto.ErrorResponse if error occurred}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/timeField/{number}")
    public ResponseEntity<TimeFieldTextResponse> readTextTimeField(
            @NotNull @PathVariable("id") int id,
            @NotNull @Valid @Min(0) @Max(2) @PathVariable("number") int number) {
        LOGGER.info("Calling readTextTimeField with id-{} and number-{}", id, number);
        TimeFieldTextResponse response = null;
        try {
            this.server.executeCommand(id, Command.READ_TEXT_FOR_TIME_FIELD, String.valueOf(number));
            response = server.findListByType(id, TimeFieldTextResponse.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().build();
    }
}
