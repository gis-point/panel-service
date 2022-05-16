package com.microgis.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class PanelWriter implements Closeable {

    private final Socket socket;

    private final PrintWriter printWriter;

    public PanelWriter(Socket socket) throws IOException {
        this.socket = socket;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream(), "windows-1251");
        this.printWriter = new PrintWriter(outputStreamWriter, true);
    }

    public void print(Object data) {
        printWriter.println(data);
    }

    @Override
    public void close() throws IOException {
        printWriter.close();
        socket.close();
    }
}
