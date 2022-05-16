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
public class ComplexCommunicationParametersCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new ComplexCommunicationParametersCommand(panelWriter, panelReader)
                .execute((panelHandler), "localhost", "login", "password", "192.168.0.1", "8080");

        //then
        verify(panelWriter).print("<KPT>4$1$localhost$login$password$192.168.0.1$8080</KPT>");
    }
}