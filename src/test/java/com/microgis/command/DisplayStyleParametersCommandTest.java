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
public class DisplayStyleParametersCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Test
    public void testExecute() {
        //when
        new DisplayStyleParametersCommand(panelWriter, panelReader)
                .execute((panelHandler), "47", "1", "1", "0", "2", "99", "1", "5", "8", "1", "15", "5", "0", "30", "10", "0",
                        "8", "8", "8", "12422", "120", "0", "0", "0", "0", "9", "0", "1", "0", "0", "46", "9", "0", "1", "2",
                        "3", "4");

        //then
        verify(panelWriter)
                .print("<KPT>306$1$47$1$1$0$2$99$1$5$8$1$15$5$0$30$10$0$8$8$8$12422$120$0$0$0$0$9$0$1$0$0$46$9$0$1$2$3$4</KPT>");
    }
}