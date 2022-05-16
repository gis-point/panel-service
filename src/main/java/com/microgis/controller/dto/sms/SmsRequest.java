package com.microgis.controller.dto.sms;

import com.microgis.validation.CheckPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {

    private String rid;

    private String source;

    @CheckPhoneNumber
    private String destination;

    private boolean serviceType = true;

    private String bearerType = "sms";

    private String contentType = "text/plain";

    private String callbackNum;

    private String content;

}