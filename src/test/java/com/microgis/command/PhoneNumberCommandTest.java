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
public class PhoneNumberCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new PhoneNumberCommand(panelWriter, panelReader).execute((panelHandler), "1", "+380507528134");

        //then
        verify(panelWriter).print("<KPT>7$1$PHONE1$+380507528134$</KPT>");
    }

    @Test
    public void testExecuteClean() {
        //when
        new PhoneNumberCommand(panelWriter, panelReader).execute((panelHandler), "1");

        //then
        verify(panelWriter).print("<KPT>7$1$PHONE1$$</KPT>");
    }
}