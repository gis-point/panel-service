package com.microgis.command;

import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TimeFieldTextCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new TimeFieldTextCommand(panelWriter, panelReader).execute((panelHandler), "0", "Arriving");

        //then
        verify(panelWriter).print("<KPT>14$1$0$Arriving</KPT>");
    }
}