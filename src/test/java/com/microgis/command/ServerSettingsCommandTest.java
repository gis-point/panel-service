package com.microgis.command;

import com.microgis.controller.dto.panel.Panel;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServerSettingsCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecuteSuccess() {
        //given
        when(panelHandler.getPanel()).thenReturn(new Panel());

        //when
        new ServerSettingsCommand(panelWriter, panelReader).execute((panelHandler), "localhost", "1");

        //then
        verify(panelWriter).print("<KPT>52$1$localhost$1</KPT>");
    }

    @Test
    public void testExecuteFailed() {
        //when
        new ServerSettingsCommand(panelWriter, panelReader).execute((panelHandler));

        //then
        verify(panelWriter, times(0)).print(anyString());
    }
}