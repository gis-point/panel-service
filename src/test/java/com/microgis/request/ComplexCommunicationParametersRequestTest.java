package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexCommunicationParametersRequestTest {

    @Test
    public void build() {
        ComplexCommunicationParametersRequest request = new ComplexCommunicationParametersRequest.Builder().addPacketNumber(1)
                .addAddress("localhost").addLogin("login").addPassword("password").addServerAddress("192.168.0.1")
                .addServerPort("8080").build();
        assertEquals("<KPT>4$1$localhost$login$password$192.168.0.1$8080</KPT>", request.toString());
    }
}