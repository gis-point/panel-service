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
public class OperationParametersCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new OperationParametersCommand(panelWriter, panelReader).execute((panelHandler), "0", "0", "0", "0", "0", "0", "0");

        //then
        verify(panelWriter).print("<KPT>310$1$0$0$0$0$0$0$0</KPT>");
    }
}