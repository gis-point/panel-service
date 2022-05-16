package com.microgis.controller;

import com.microgis.controller.dto.RoutesModeResponse;
import com.microgis.controller.dto.panel.Command;
import com.microgis.controller.dto.sms.SmsResponse;
import com.microgis.server.Server;
import com.microgis.service.SmsService;
import com.microgis.util.Constants;
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

import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
public class SmsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsController.class);

    private final SmsService smsService;

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Sends text message to the panel by phone number
     *
     * @param id      panelId
     * @param command command to execute
     * @return response and status code
     */
    @GetMapping("/sms/{id}/command/{command}")
    public ResponseEntity<SmsResponse> sendSmsToPanel(@NotNull @PathVariable("id") Integer id,
                                                      @NotNull @PathVariable("command") Command command) {
        LOGGER.info("Calling sendSmsToPanel with id-{} and command-{}", id, command);
        ResponseEntity<SmsResponse> smsResponse = null;
        try {
            smsResponse = smsService.sendMessage(findPhoneNumber(id), commandToText(command));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            exceptionProcessor.handleException(e);
        }
        return smsResponse != null ? smsResponse : ResponseEntity.badRequest().build();
    }

    /**
     * Finds text for message by command
     *
     * @param command command to execute
     * @return message text
     */
    private String commandToText(Command command) {
        if (command.equals(Command.RELOAD)) {
            return Constants.RELOAD;
        } else if (command.equals(Command.RESTART_MODEM)) {
            return Constants.RESTART_MODEM;
        } else if (command.equals(Command.SYNC_PACKAGE)) {
            return Constants.SYNC_PACKAGE;
        }
        return null;
    }

    /**
     * Finds phone number which was register in system
     *
     * @param panelId panel id
     * @return phone number
     */
    private String findPhoneNumber(int panelId) {
        return server.getModeResponses().stream()
                .filter(panel -> panel.getNumber() == panelId)
                .findFirst()
                .map(RoutesModeResponse::getPhoneNumber).orElse(null);
    }

}