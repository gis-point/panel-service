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
public class AdditionalServerSettingsCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new AdditionalServerSettingsCommand(panelWriter, panelReader).execute((panelHandler), "localhost", "1");

        //then
        verify(panelWriter).print("<KPT>400$1$localhost$1</KPT>");
    }

    @Test
    public void testExecuteEmpty() {
        //when
        new AdditionalServerSettingsCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print("<KPT>400$1$$</KPT>");
    }
}