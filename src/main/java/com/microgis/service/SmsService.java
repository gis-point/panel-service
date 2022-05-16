package com.microgis.service;

import com.microgis.controller.dto.sms.SmsRequest;
import com.microgis.controller.dto.sms.SmsResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    //TODO: need real url
    private static final String URL = "http://{environment}/api/contents";

    private final RestTemplate restTemplate;

    /**
     * Sends sms to panel by phone number
     *
     * @param number phone number
     * @param text   text to send
     * @return response and status
     */
    public ResponseEntity<SmsResponse> sendMessage(String number, String text) {
        if (text == null || number == null) {
            return ResponseEntity.badRequest().build();
        }
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setDestination(number);
        smsRequest.setContent(text);
        LOGGER.info("Created sms body - {}", smsRequest);
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<Object> request = new HttpEntity<>(smsRequest, requestHeaders);
        return restTemplate.exchange(URL, HttpMethod.POST, request, SmsResponse.class);
    }
}