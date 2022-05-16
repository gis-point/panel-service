package com.microgis.controller;

import com.microgis.controller.dto.RunningTextResponse;
import com.microgis.controller.dto.panel.Command;
import com.microgis.response.PhoneNumberResponse;
import com.microgis.response.PhysicalInfoResponse;
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
public class PanelSettingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelSettingsController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing SET_PHYSICAL_INFO(302) command
     * Set new setting into the panel
     *
     * @param id                   panel id
     * @param physicalInfoResponse see {@link PhysicalInfoResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/physical/setting")
    public ResponseEntity<?> adjustPanelViewSettings(@NotNull @PathVariable("id") Integer id,
                                                     @Valid @RequestBody PhysicalInfoResponse physicalInfoResponse) {
        LOGGER.info("Calling adjustPanelViewSettings with id-{} and physicalInfoResponse-{}", id, physicalInfoResponse);
        try {
            this.server.executeCommand(id, Command.SET_PHYSICAL_INFO, String.valueOf(physicalInfoResponse.getTextSpeed()),
                    String.valueOf(physicalInfoResponse.getMaxBrightness()),
                    String.valueOf(physicalInfoResponse.getMinBrightness()));
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing SET_PHONE_NUMBER(7) command
     * Set new phone numbers into panel slots by id
     *
     * @param id                  panel id
     * @param phoneNumberResponse see {@link PhoneNumberResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @PostMapping("/{id}/phone/setting")
    public ResponseEntity<?> setPhoneNumberIntoPanel(@NotNull @PathVariable("id") Integer id,
                                                     @RequestBody @Valid PhoneNumberResponse phoneNumberResponse) {
        LOGGER.info("Calling setPhoneNumberIntoPanel with id-{} and phoneNumberResponse-{}", id, phoneNumberResponse);
        try {
            this.server.executeCommand(id, Command.SET_PHONE_NUMBER, String.valueOf(phoneNumberResponse.getNumber()),
                    phoneNumberResponse.getPhoneNumber());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    /**
     * Processing STATIC_TEXT(16) and RUNNING_LINE_TEXT(10) commands
     * Set running text in route row or static text in entire panel if route filed empty or null
     *
     * @param id                  panel id
     * @param runningTextResponse see {@link RunningTextResponse class}
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel or route were not found returns 400
     */
    @PostMapping("/{id}/text/setting")
    public ResponseEntity<?> sendText(@NotNull @PathVariable("id") Integer id,
                                      @RequestBody @Valid RunningTextResponse runningTextResponse) {
        LOGGER.info("Calling sendText with id-{} and runningTextResponse-{}", id, runningTextResponse);
        try {
            if (runningTextResponse.getRoute() == null || runningTextResponse.getRoute().isEmpty()) {
                this.server.executeCommand(id, Command.STATIC_TEXT, runningTextResponse.getText());
            } else {
                String route = runningTextResponse.getRoute();
                this.server.executeCommand(id, Command.RUNNING_LINE_TEXT, route, runningTextResponse.getText(),
                        String.valueOf(runningTextResponse.isAddToExistingMode()));
            }
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }
}
