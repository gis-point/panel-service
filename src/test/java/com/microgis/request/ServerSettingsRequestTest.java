package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServerSettingsRequestTest {

    @Test
    public void build() {
        ServerSettingsRequest request = new ServerSettingsRequest.Builder().addPacketNumber(1).addIP("localhost").addPort("1")
                .build();
        assertEquals("<KPT>52$1$localhost$1</KPT>", request.toString());
    }
}