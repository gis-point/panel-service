package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhoneNumberRequestTest {

    @Test
    public void buildWithNumber() {
        PhoneNumberRequest request = new PhoneNumberRequest.Builder().addPacketNumber(1).addNumber(1)
                .addPhoneNumber("+380507528134").build();
        assertEquals("<KPT>7$1$PHONE1$+380507528134$</KPT>", request.toString());
    }

    @Test
    public void buildWithoutNumber() {
        PhoneNumberRequest request = new PhoneNumberRequest.Builder().addPacketNumber(1).addNumber(1).addPhoneNumber("")
                .build();
        assertEquals("<KPT>7$1$PHONE1$$</KPT>", request.toString());
    }
}