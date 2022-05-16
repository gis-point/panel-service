package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdjustPhysicalInfoRequestTest {

    @Test
    public void build() {
        AdjustPhysicalInfoRequest request = new AdjustPhysicalInfoRequest.Builder().addPacketNumber(1).addTextSpeed(40)
                .addMaxBrightness(255).addMinBrightness(255).build();
        assertEquals("<KPT>302$1$40$255$255</KPT>", request.toString());
    }
}