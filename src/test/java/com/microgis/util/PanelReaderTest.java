package com.microgis.util;

import com.microgis.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PanelReaderTest {

    @Mock
    private Socket socket;

    @Test
    public void testReadSuccess() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("<KPT>1$1022$1$4.02$3$1504$0$24$96$0$865733022082887$2$1</KPT>".getBytes());
        when(socket.getInputStream()).thenReturn(inputStream);
        PanelReader panelReader = new PanelReader(socket);

        //when
        Response result = panelReader.read();
        panelReader.close();

        //then
        assertEquals("TICKET", result.getCommand().name());
        assertEquals(1, result.getCommand().getCommandNumber());
        assertEquals(0, result.getPanelId());
        assertNotNull(result.getData());
    }

    @Test
    public void testReadFailed() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("-1".getBytes());
        when(socket.getInputStream()).thenReturn(inputStream);
        PanelReader panelReader = new PanelReader(socket);

        //when
        Response result = panelReader.read();
        panelReader.close();

        //then
        assertNull(result);
    }
}