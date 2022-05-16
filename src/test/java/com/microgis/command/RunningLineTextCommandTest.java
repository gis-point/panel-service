package com.microgis.command;

import com.microgis.controller.dto.RunningTextResponse;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.server.PanelHandler;
import com.microgis.server.Server;
import com.microgis.service.PredictionService;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RunningLineTextCommandTest {

    @Mock
    protected PanelHandler panelHandler;

    @Mock
    protected PanelReader panelReader;

    @Mock
    protected PanelWriter panelWriter;

    @Mock
    protected Server server;

    @Mock
    protected PredictionService predictionService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    public void testExecute() {
        //given
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(PanelTestFixtures.createPredictionResponse().getPredictions());
        when(panelHandler.getPanel()).thenReturn(PanelTestFixtures.createPanel());

        //when
        new RunningLineTextCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print(stringArgumentCaptor.capture());
        assertTrue(new ArrayList<>(stringArgumentCaptor.getAllValues()).get(0).endsWith("$0$||||$1$Т|07|Погулянка |13 хв||0|0|$2$Т|09|Залізничний вокзал |39 хв||0|0|$3$А|29|Винники. Винниченка |2 хв||0|0|$4$А|37|Хоткевича |7 хв||0|0|</KPT>"));
    }

    @Test
    public void testExecuteResetStatic() {
        //given
        Panel panel = PanelTestFixtures.createPanel();
        panel.setStaticText("");
        panel.setStaticTextMode(true);
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(PanelTestFixtures.createPredictionResponse().getPredictions());
        when(panelHandler.getPanel()).thenReturn(panel);

        //when
        new RunningLineTextCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print("<KPT>17$1</KPT>");
    }

    @Test
    public void testExecuteWithRunningTextLine() {
        //given
        Panel panel = PanelTestFixtures.createPanel();
        panel.setRunningTextMode(true);
        panel.setRunningTextForRoute(Collections.singletonList(new RunningTextResponse("Т07", "TEXT", false)));
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(PanelTestFixtures.createPredictionResponse().getPredictions());
        when(panelHandler.getPanel()).thenReturn(panel);

        //when
        new RunningLineTextCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print(stringArgumentCaptor.capture());
        assertTrue(new ArrayList<>(stringArgumentCaptor.getAllValues()).get(0).endsWith("$0$||||$1$Т|07|TEXT" +
                "$2$Т|09|Залізничний вокзал |39 хв||0|0|" +
                "$3$А|29|Винники. Винниченка |2 хв||0|0|" +
                "$4$А|37|Хоткевича |7 хв||0|0|</KPT>"));
    }

    @Test
    public void testExecuteWithRunningTextLineNonexistentRoute() {
        //given
        Panel panel = PanelTestFixtures.createPanel();
        panel.setRunningTextMode(true);
        panel.setRunningTextForRoute(Collections.singletonList(new RunningTextResponse("Т10", "TEXT", false)));
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(PanelTestFixtures.createPredictionResponse().getPredictions());
        when(panelHandler.getPanel()).thenReturn(panel);

        //when
        new RunningLineTextCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print(stringArgumentCaptor.capture());
        assertTrue(new ArrayList<>(stringArgumentCaptor.getAllValues()).get(0).endsWith("0$||||$1$Т|07|Погулянка |13 хв||0|0|" +
                "$2$Т|09|Залізничний вокзал |39 хв||0|0|" +
                "$3$А|29|Винники. Винниченка |2 хв||0|0|" +
                "$4$А|37|Хоткевича |7 хв||0|0|" +
                "$5$Т|10|TEXT</KPT>"));
    }

    @Test
    public void testExecuteWithRunningTextLineWithArrivalTime() {
        //given
        Panel panel = PanelTestFixtures.createPanel();
        panel.setRunningTextMode(true);
        panel.setRunningTextForRoute(Collections.singletonList(new RunningTextResponse("Т07", "TEXT", true)));
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(PanelTestFixtures.createPredictionResponse().getPredictions());
        when(panelHandler.getPanel()).thenReturn(panel);

        //when
        new RunningLineTextCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print(stringArgumentCaptor.capture());
        assertTrue(new ArrayList<>(stringArgumentCaptor.getAllValues()).get(0).endsWith("$0$||||$1$Т|07|TEXT|13 хв||0|0|" +
                "$2$Т|09|Залізничний вокзал |39 хв||0|0|" +
                "$3$А|29|Винники. Винниченка |2 хв||0|0|" +
                "$4$А|37|Хоткевича |7 хв||0|0|</KPT>"));
    }

    @Test
    public void testExecutePostStatic() {
        //given
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(panelHandler.getPanel()).thenReturn(new Panel());

        //when
        new RunningLineTextCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter).print("<KPT>16$1$</KPT>");
    }
}