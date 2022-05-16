package com.microgis.service;

import com.microgis.command.PanelTestFixtures;
import com.microgis.configuration.AppProperties;
import com.microgis.controller.dto.prediction.Prediction;
import com.microgis.controller.dto.prediction.StopInfo;
import com.microgis.document.Panels;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransitClockPredictionServiceTest {

    private static final String URL = "http://168.119.149.120:8383/api/v1/key/f78a2e9a/agency/1/command/predictions?rs=4997&numPreds=3&format=json";
    private static final String URL_FAILED = "http://168.119.149.120:8383/api/v1/key/f78a2e9a/agency/1/command/predictions?rs=94|44976&rs=118|44976&rs=130|44976&rs=1022|44976&rs=977|44976&rs=1594|44976&rs=976|44976&numPreds=3";
    private static final String URL_SUCCESS = "http://168.119.149.120:8383/api/v1/key/f78a2e9a/agency/1/command/predictions?rs=94|44976&rs=118|44976&rs=130|44976&rs=1022|44976&rs=977|44976&rs=1594|44976&rs=1628|44976&numPreds=3";

    @InjectMocks
    private TransitClockPredictionService predictionService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PanelGtfsService panelGtfsService;

    @Mock
    private AppProperties appProperties;

    @Test
    public void testGetPredictionsSuccess() {
        //given
        ResponseEntity<StopInfo> responseEntity = new ResponseEntity<>(PanelTestFixtures.createPredictionResponse(), HttpStatus.OK);
        when(restTemplate.getForEntity(URL, StopInfo.class)).thenReturn(responseEntity);
        when(panelGtfsService.findByNumber(4997)).thenReturn(PanelTestFixtures.createPanels().get(5));
        when(appProperties.getPredictionServerBaseUrl()).thenReturn("http://168.119.149.120:8383");

        //when
        List<Prediction> predictions = predictionService.getPredictions(4997);

        //then
        assertEquals(4, predictions.size());

        assertEquals("А29", predictions.get(0).getRouteShortName());
        assertEquals("А29 - Залізничний вокзал - Винники Івасюка", predictions.get(0).getRouteName());
        assertEquals("1014", predictions.get(0).getRouteId());
        assertEquals("4997", predictions.get(0).getStopId());
        assertEquals("Підвальна (59)", predictions.get(0).getStopName());
        assertEquals(59, predictions.get(0).getStopCode().intValue());

        assertEquals("А37", predictions.get(1).getRouteShortName());
        assertEquals("А37 - вул. Під Голоском - вул. Хоткевича", predictions.get(1).getRouteName());
        assertEquals("126", predictions.get(1).getRouteId());
        assertEquals("4997", predictions.get(1).getStopId());
        assertEquals("Підвальна (59)", predictions.get(1).getStopName());
        assertEquals(59, predictions.get(1).getStopCode().intValue());

        assertEquals("Т07", predictions.get(2).getRouteShortName());
        assertEquals("Т07 - Церква Анни - Погулянка", predictions.get(2).getRouteName());
        assertEquals("950", predictions.get(2).getRouteId());
        assertEquals("4997", predictions.get(2).getStopId());
        assertEquals("Підвальна (59)", predictions.get(2).getStopName());
        assertEquals(59, predictions.get(2).getStopCode().intValue());

        assertEquals("Т09", predictions.get(3).getRouteShortName());
        assertEquals("Т09 - Залізничний вокзал - вул. Торф'яна", predictions.get(3).getRouteName());
        assertEquals("1631", predictions.get(3).getRouteId());
        assertEquals("4997", predictions.get(3).getStopId());
        assertEquals("Підвальна (59)", predictions.get(3).getStopName());
        assertEquals(59, predictions.get(3).getStopCode().intValue());

    }

    @Test
    public void testGetPredictionsEmpty() {
        //given
        ResponseEntity<StopInfo> responseEntity = new ResponseEntity<>(PanelTestFixtures.createPredictionResponseZero(), HttpStatus.OK);
        when(restTemplate.getForEntity(URL, StopInfo.class)).thenReturn(responseEntity);
        when(panelGtfsService.findByNumber(4997)).thenReturn(new Panels());
        when(appProperties.getPredictionServerBaseUrl()).thenReturn("http://168.119.149.120:8383");

        //when
        List<Prediction> predictions = predictionService.getPredictions(4997);

        //then
        assertEquals(0, predictions.size());
    }

    @Test
    public void testSecondChanceCallSuccess() {
        //given
        when(panelGtfsService.findByNumber(44976)).thenReturn(PanelTestFixtures.createPanels().get(1));
        ResponseEntity<StopInfo> responseEntity = new ResponseEntity<>(PanelTestFixtures.createPredictionResponse(), HttpStatus.OK);
        when(restTemplate.getForEntity(URL_SUCCESS, StopInfo.class)).thenReturn(responseEntity);
        Panels panels = PanelTestFixtures.createPanels().get(1);
        panels.getRoutes().get(0).setGtfs(9);
        when(appProperties.getPredictionServerBaseUrl()).thenReturn("http://168.119.149.120:8383");

        //when
        List<Prediction> predictions = predictionService.getPredictions(44976);

        //then
        assertEquals(4, predictions.size());

        assertEquals("А29", predictions.get(0).getRouteShortName());
        assertEquals("А29 - Залізничний вокзал - Винники Івасюка", predictions.get(0).getRouteName());
        assertEquals("1014", predictions.get(0).getRouteId());
        assertEquals("4997", predictions.get(0).getStopId());
        assertEquals("Підвальна (59)", predictions.get(0).getStopName());
        assertEquals(59, predictions.get(0).getStopCode().intValue());

        assertEquals("А37", predictions.get(1).getRouteShortName());
        assertEquals("А37 - вул. Під Голоском - вул. Хоткевича", predictions.get(1).getRouteName());
        assertEquals("126", predictions.get(1).getRouteId());
        assertEquals("4997", predictions.get(1).getStopId());
        assertEquals("Підвальна (59)", predictions.get(1).getStopName());
        assertEquals(59, predictions.get(1).getStopCode().intValue());

        assertEquals("Т07", predictions.get(2).getRouteShortName());
        assertEquals("Т07 - Церква Анни - Погулянка", predictions.get(2).getRouteName());
        assertEquals("950", predictions.get(2).getRouteId());
        assertEquals("4997", predictions.get(2).getStopId());
        assertEquals("Підвальна (59)", predictions.get(2).getStopName());
        assertEquals(59, predictions.get(2).getStopCode().intValue());

        assertEquals("Т09", predictions.get(3).getRouteShortName());
        assertEquals("Т09 - Залізничний вокзал - вул. Торф'яна", predictions.get(3).getRouteName());
        assertEquals("1631", predictions.get(3).getRouteId());
        assertEquals("4997", predictions.get(3).getStopId());
        assertEquals("Підвальна (59)", predictions.get(3).getStopName());
        assertEquals(59, predictions.get(3).getStopCode().intValue());
    }

    @Test
    public void testSecondChanceCallFailed() {
        //given
        when(panelGtfsService.findByNumber(44976)).thenReturn(PanelTestFixtures.createPanels().get(1));
        ResponseEntity<StopInfo> responseEntity = new ResponseEntity<>(PanelTestFixtures.createPredictionResponseZero(), HttpStatus.OK);
        when(restTemplate.getForEntity(URL_SUCCESS, StopInfo.class)).thenReturn(responseEntity);
        Panels panels = PanelTestFixtures.createPanels().get(1);
        panels.getRoutes().get(0).setGtfs(9);
        when(appProperties.getPredictionServerBaseUrl()).thenReturn("http://168.119.149.120:8383");

        //when
        List<Prediction> predictions = predictionService.getPredictions(44976);

        //then
        assertTrue(CollectionUtils.isEmpty(predictions));
    }
}