package com.microgis.controller;

import com.microgis.controller.dto.panel.Command;
import com.microgis.server.Server;
import com.microgis.util.ExceptionProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/panel-service")
@SuppressWarnings("java:S1452")
public class PanelResetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelResetController.class);

    private final Server server;

    private final ExceptionProcessor exceptionProcessor;

    /**
     * Processing RELOAD(5), RESET_ALL_SETTINGS(8), RESET_TICKER(11), RESET_STATIC_TEXT(17) and RESTART_MODEM(555) commands
     * Reset one of the commands which above, resets only one command in one request
     *
     * @param id       panel id
     * @param reload   reload panel
     * @param settings reset all settings to default values
     * @param ticker   reset running text line
     * @param text     reset static text
     * @return 200 with empty body if everything is ok,
     * in case of error {@link com.microgis.controller.dto.ErrorResponse}
     * if panel wasn't found returns 400
     */
    @GetMapping("/{id}/reset")
    public ResponseEntity<?> reset(@NotNull @PathVariable("id") Integer id,
                                   @RequestParam(value = "reload", required = false, defaultValue = "false") boolean reload,
                                   @RequestParam(value = "settings", required = false, defaultValue = "false") boolean settings,
                                   @RequestParam(value = "ticker", required = false, defaultValue = "false") boolean ticker,
                                   @RequestParam(value = "text", required = false, defaultValue = "false") boolean text,
                                   @RequestParam(value = "modem", required = false, defaultValue = "false") boolean modem,
                                   @RequestParam(value = "route", required = false) String route) {
        Command command = findCommand(reload, settings, ticker, text, modem);
        LOGGER.info("Calling reset with id-{} and command-{}", id, command);
        try {
            if (ticker && route == null) {
                return ResponseEntity.badRequest().build();
            }
            if (command != null) {
                this.server.executeCommand(id, command, route);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return exceptionProcessor.handleException(e);
        }
    }

    private Command findCommand(boolean reload, boolean settings, boolean ticker, boolean text, boolean modem) {
        if (reload) {
            return Command.RELOAD;
        } else if (settings) {
            return Command.RESET_ALL_SETTINGS;
        } else if (ticker) {
            return Command.RESET_TICKER;
        } else if (text) {
            return Command.RESET_STATIC_TEXT;
        } else if (modem) {
            return Command.RESTART_MODEM;
        }
        return null;
    }
}