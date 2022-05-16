package com.microgis.service;

import com.microgis.command.PanelTestFixtures;
import com.microgis.controller.dto.sms.SmsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SmsServiceTest {

    private static final String URL = "http://{environment}/api/contents";

    @InjectMocks
    private SmsService smsService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void testSendSmsSuccess() {
        //given
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<Object> request = new HttpEntity<>(PanelTestFixtures.createSmsRequest(), requestHeaders);
        ResponseEntity<SmsResponse> responseEntity =
                new ResponseEntity<>(PanelTestFixtures.createSmsResponse(), HttpStatus.ACCEPTED);
        when(restTemplate.exchange(URL, HttpMethod.POST, request, SmsResponse.class))
                .thenReturn(responseEntity);

        //when
        ResponseEntity<SmsResponse> response = smsService.sendMessage("380606301237", "text");

        //then
        assertEquals(202, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("904705f0-3c5e-4556-81b7fd2e3d83", response.getBody().getMid());
    }

    @Test
    public void testSendSmsFailed() {
        //given
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<Object> request = new HttpEntity<>(PanelTestFixtures.createSmsRequest(), requestHeaders);
        ResponseEntity<SmsResponse> responseEntity =
                new ResponseEntity<>(PanelTestFixtures.createSmsResponseError(), HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(URL, HttpMethod.POST, request, SmsResponse.class))
                .thenReturn(responseEntity);

        //when
        ResponseEntity<SmsResponse> response = smsService.sendMessage("380606301237", "text");

        //then
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("904705f0-3c5e-4556-81b7fd2e3d83", response.getBody().getMid());
        assertEquals(1019, response.getBody().getErrorId().intValue());
        assertEquals("Invalid destination address", response.getBody().getErrorMsg());
    }
}