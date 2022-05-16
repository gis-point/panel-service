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
public class DisplayStyleScheduleParameterCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new DisplayStyleScheduleParameterCommand(panelWriter, panelReader)
                .execute((panelHandler), "21", "94", "2", "91", "1", "98", "22", "3", "1", "1", "1", "1", "1", "2", "1", "1",
                        "2", "9", "3", "3", "1", "0");

        //then
        verify(panelWriter).print("<KPT>304$1$21$94$2$91$1$98$22$3$1$1$1$1$1$2$1$1$2$9$3$3$1$0</KPT>");
    }
}