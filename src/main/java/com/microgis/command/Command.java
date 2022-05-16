package com.microgis.command;

import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;

public abstract class Command {

    protected final PanelWriter writer;

    protected final PanelReader reader;

    protected final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    protected Command(PanelWriter writer, PanelReader reader) {
        this.writer = writer;
        this.reader = reader;
    }

    public abstract void execute(PanelHandler handler, String... data) throws IOException;

}