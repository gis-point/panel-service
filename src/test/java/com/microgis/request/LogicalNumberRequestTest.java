package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogicalNumberRequestTest {

    @Test
    public void build() {
        LogicalNumberRequest request = new LogicalNumberRequest.Builder().addPacketNumber(1).addLogicalNumber(1111).build();
        assertEquals("<KPT>404$1$1111</KPT>", request.toString());
    }
}