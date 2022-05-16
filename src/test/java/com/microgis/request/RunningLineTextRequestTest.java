package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RunningLineTextRequestTest {

    final String packet = "<KPT>10$1022$1$1$Підвальна</KPT>";
    final String text = "Підвальна";

    @Test
    public void test() {
        RunningLineTextRequest request = new RunningLineTextRequest.Builder()
                .addPacketNumber(1022)
                .addTextPart(1)
                .addTextPartCount(1)
                .addText(text).build();
        assertEquals(packet, request.toString());
    }
}