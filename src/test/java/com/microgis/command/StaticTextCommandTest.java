package com.microgis.command;

import com.microgis.controller.dto.panel.Panel;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StaticTextCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        when(panelHandler.getPanel()).thenReturn(new Panel());
        new StaticTextCommand(panelWriter, panelReader).execute((panelHandler), "text");

        //then
        verify(panelWriter).print("<KPT>16$1$text</KPT>");
    }
}