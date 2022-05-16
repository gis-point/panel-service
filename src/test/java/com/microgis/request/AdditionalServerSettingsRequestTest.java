package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdditionalServerSettingsRequestTest {

    @Test
    public void build() {
        AdditionalServerSettingsRequest request = new AdditionalServerSettingsRequest.Builder().addPacketNumber(1)
                .addIP("localhost").addPort("1").build();
        assertEquals("<KPT>400$1$localhost$1</KPT>", request.toString());
    }
}