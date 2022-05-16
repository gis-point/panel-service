package com.microgis.controller.dto.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponse {

    private String mid;

    private Integer errorId;

    private String errorMsg;

}