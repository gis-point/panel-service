package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeFieldTextRequestTest {

    @Test
    public void build() {
        TimeFieldTextRequest request = new TimeFieldTextRequest.Builder().addPacketNumber(1).addNumber("0").addText("text")
                .build();
        assertEquals("<KPT>14$1$0$text</KPT>", request.toString());
    }
}