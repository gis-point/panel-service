package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StopNameRequestTest {

    @Test
    public void build() {
        StopNameRequest request = new StopNameRequest.Builder().addPacketNumber(1).addStopName("stop name").build();
        assertEquals("<KPT>60$1$stop name</KPT>", request.toString());
    }
}