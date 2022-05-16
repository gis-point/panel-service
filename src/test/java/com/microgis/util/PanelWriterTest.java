package com.microgis.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PanelWriterTest {

    @Mock
    private Socket socket;

    @Test
    public void testPrint() throws IOException {
        //given
        OutputStream outputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(outputStream);
        PanelWriter panelWriter = spy(new PanelWriter(socket));

        //when
        panelWriter.print("<KPT>1$1022$1$4.02$3$1504$0$24$96$0$865733022082887$2$1</KPT>");
        panelWriter.close();

        //then
        verify(panelWriter, times(1)).print("<KPT>1$1022$1$4.02$3$1504$0$24$96$0$865733022082887$2$1</KPT>");
    }
}