package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StaticTextRequestTest {

    @Test
    public void build() {
        StaticTextRequest request = new StaticTextRequest.Builder().addPacketNumber(1).addText("any").build();
        assertEquals("<KPT>16$1$any</KPT>", request.toString());
    }
}