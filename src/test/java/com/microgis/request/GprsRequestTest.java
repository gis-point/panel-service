package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GprsRequestTest {

    @Test
    public void build() {
        GprsRequest request = new GprsRequest.Builder().addPacketNumber(1).addAddress("address").addLogin("login")
                .addPassword("password").build();
        assertEquals("<KPT>50$1$address$login$password</KPT>", request.toString());
    }
}