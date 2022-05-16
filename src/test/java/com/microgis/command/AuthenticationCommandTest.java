package com.microgis.command;

import com.microgis.controller.dto.RoutesModeResponse;
import com.microgis.response.Response;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationCommandTest {

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
    public void testExecute() throws IOException {
        //given
        Response response = new Response(new StringBuilder("<KPT>1$1$4997$4.12$4$1504$0(0)$24$96$0$$1002$4997$LAN</KPT>"));
        when(panelHandler.getServer()).thenReturn(server);
        when(panelReader.read()).thenReturn(response);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(PanelTestFixtures.createPredictionResponse().getPredictions());
        when(panelHandler.getPanel()).thenReturn(PanelTestFixtures.createPanel());

        //when
        new AuthenticationCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter, times(2)).print(stringArgumentCaptor.capture());
        List<String> result = stringArgumentCaptor.getAllValues();
        assertEquals(2, result.size());
        assertTrue(result.get(0).startsWith("<KPT>2$1$"));
        assertEquals("<KPT>60$1$Підвальна (59)</KPT>", result.get(1));
    }

    @Test
    public void testExecutePredictionZero() throws IOException {
        //given
        Response response = new Response(new StringBuilder("<KPT>306$1$47$1$1$0$2$99$1$5$8$1$15$5$0$30$10$0$8$8$8$12422$120$0$0$0$0$9$0$1$0$0$46$9$0$1$2$3$4$</KPT>"));
        List<RoutesModeResponse> modeResponses = new CopyOnWriteArrayList<>();
        RoutesModeResponse routesModeResponse = new RoutesModeResponse();
        routesModeResponse.setStopName("Підвальна (59)");
        routesModeResponse.setNumber(4997);
        modeResponses.add(routesModeResponse);
        when(panelHandler.getServer()).thenReturn(server);
        when(server.getModeResponses()).thenReturn(modeResponses);
        when(panelReader.read()).thenReturn(response);
        when(server.getPredictionService()).thenReturn(predictionService);
        when(predictionService.getPredictions(4997)).thenReturn(new ArrayList<>());
        when(panelHandler.getPanel()).thenReturn(PanelTestFixtures.createPanel());

        //when
        new AuthenticationCommand(panelWriter, panelReader).execute(panelHandler);

        //then
        verify(panelWriter, times(2)).print(stringArgumentCaptor.capture());
        List<String> result = stringArgumentCaptor.getAllValues();
        assertEquals(2, result.size());
        assertTrue(result.get(0).startsWith("<KPT>2$1$"));
        assertEquals("<KPT>60$1$Підвальна (59)</KPT>", result.get(1));
    }
}