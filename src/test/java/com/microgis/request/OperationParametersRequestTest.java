package com.microgis.request;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperationParametersRequestTest {

    @Test
    public void build() {
        OperationParametersRequest request = new OperationParametersRequest.Builder().addPacketNumber(1).addSortMode("0")
                .addServerTimeout("0").addKeepRouteRecordTime("0").addStartEveningTime("0").addFinishEveningTime("0")
                .addAdditionalServerInterval("0").addAdditionalServerConnectionInterval("0").build();
        assertEquals("<KPT>310$1$0$0$0$0$0$0$0</KPT>", request.toString());
    }
}