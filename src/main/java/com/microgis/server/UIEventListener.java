package com.microgis.server;

import com.microgis.controller.dto.panel.Command;

import java.io.IOException;

@FunctionalInterface
public interface UIEventListener {
    void execute(Command command, String... data) throws IOException;
}
