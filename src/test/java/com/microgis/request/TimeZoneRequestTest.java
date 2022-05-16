package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeZoneRequestTest {

    @Test
    public void build() {
        TimeZoneRequest request = new TimeZoneRequest.Builder()
                .addPacketNumber(1)
                .addSummerTimeMode("0")
                .addTimeZone("-12")
                .build();
        assertEquals("<KPT>320$1$0$-12</KPT>", request.toString());
    }
}