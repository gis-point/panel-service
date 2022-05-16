package com.microgis.request;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class IdentificationRequestTest {

    final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    final String packet = "<KPT>2$1$30.10.2019 10:55:14</KPT>";
    Date date;

    @Before
    public void setUp() throws Exception {
        date = formatter.parse("30.10.2019 10:55:14");
    }

    @Test
    public void build() {
        IdentificationRequest request = new IdentificationRequest.Builder()
                .addPacketNumber(1)
                .addDate(date).build();
        assertEquals(packet, request.toString());
    }
}