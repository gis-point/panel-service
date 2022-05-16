package com.microgis.util;

import com.microgis.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PanelReader implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelReader.class);

    private final Socket socket;

    private final InputStreamReader reader;

    private final StringBuilder dataBuffer = new StringBuilder();

    private final StringBuilder response = new StringBuilder();

    public PanelReader(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new InputStreamReader(this.socket.getInputStream(), "windows-1251");
    }

    public Response read() throws IOException {
        int character;
        while ((character = reader.read()) != -1) {
            dataBuffer.append((char) character);
            int startIndex = dataBuffer.indexOf(Constants.START);
            int endIndex = dataBuffer.indexOf(Constants.END);
            if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                response.setLength(0);
                response.append(dataBuffer.substring(startIndex, endIndex + Constants.END.length()));
                dataBuffer.replace(startIndex, endIndex + Constants.END.length(), "");
                break;
            }
        }
        if (character == -1) {
            return null;
        }
        String command = response.toString();
        LOGGER.debug("Response in reader - {}", command);
        return new Response(response);
    }

    @Override
    public void close() throws IOException {
        reader.close();
        socket.close();
    }
}
