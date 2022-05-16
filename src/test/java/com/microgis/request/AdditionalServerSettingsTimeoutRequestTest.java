package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdditionalServerSettingsTimeoutRequestTest {

    @Test
    public void build() {
        AdditionalServerSettingsTimeoutRequest request = new AdditionalServerSettingsTimeoutRequest.Builder().addPacketNumber(1)
                .addTimeout("0").build();
        assertEquals("<KPT>402$1$0</KPT>", request.toString());
    }
}