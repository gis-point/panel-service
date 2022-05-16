package com.microgis.service;

import com.microgis.command.PanelTestFixtures;
import com.microgis.configuration.AppProperties;
import com.microgis.controller.dto.GtfsResponse;
import com.microgis.document.Panels;
import com.microgis.document.repository.PanelRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PanelGtfsServiceTest {

    private static final String URL = "http://168.119.149.120:8383/api/v1/key/f78a2e9a/agency/1/command/routes?format=json";

    @InjectMocks
    private PanelGtfsService panelGtfsService;

    @Mock
    private PanelRepository panelRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AppProperties appProperties;

    @Test
    public void testFindGtfs() {
        //given
        when(appProperties.getPredictionServerBaseUrl()).thenReturn("http://168.119.149.120:8383");
        when(panelRepository.findByNumber(44976)).thenReturn(Optional.of(PanelTestFixtures.createPanels().get(1)));
        ResponseEntity<GtfsResponse> responseEntity = new ResponseEntity<>(PanelTestFixtures.createGtfsResponse(), HttpStatus.OK);
        when(restTemplate.getForEntity(URL, GtfsResponse.class)).thenReturn(responseEntity);

        //when
        Panels result = panelGtfsService.findGtfs(44976);

        //then
        assertEquals(9, result.getRoutes().get(0).getGtfs().intValue());
        verify(panelRepository, times(1)).save(result);
    }

    @Test
    public void testFindGtfsNull() {
        //given
        when(appProperties.getPredictionServerBaseUrl()).thenReturn("http://168.119.149.120:8383");
        ResponseEntity<GtfsResponse> responseEntity = new ResponseEntity<>(PanelTestFixtures.createGtfsResponse(), HttpStatus.OK);
        when(restTemplate.getForEntity(URL, GtfsResponse.class)).thenReturn(responseEntity);

        //when then
        assertNull(panelGtfsService.findGtfs(1));
    }
}