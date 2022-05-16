package com.microgis.request;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RouteLineRequestTest {

    final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    final String packet = "<KPT>3$1$30.10.2019 10:55:14$1$Т|07|вул. Татарбунарська - вул. Пасічна|6 хв|0|0|$2$А|39|ВУЛ. ГРІНЧЕНКА - КРИВЧИЦІ|11 хв|0|0|$3$А|57|вул. Максимовича - вул. Польова|43 хв|0|0|</KPT>";
    Date date;

    @Before
    public void setUp() throws Exception {
        date = formatter.parse("30.10.2019 10:55:14");
    }

    @Test
    public void test() {
        RouteLineRequest request = new RouteLineRequest.Builder()
                .addPacketNumber(1)
                .addDate(date)
                .addScheduleLineNumber(1)
                .addDeviceType("Т")
                .addRouteNumber("07")
                .addRouteName("вул. Татарбунарська - вул. Пасічна")
                .addArrivalMinutes("6 хв")
                .addDateType(0)
                .addLowFloor(false)
                .addScheduleLineNumber(2)
                .addDeviceType("А")
                .addRouteNumber("39")
                .addRouteName("ВУЛ. ГРІНЧЕНКА - КРИВЧИЦІ")
                .addArrivalMinutes("11 хв")
                .addDateType(0)
                .addLowFloor(false)
                .addScheduleLineNumber(3)
                .addDeviceType("А")
                .addRouteNumber("57")
                .addRouteName("вул. Максимовича - вул. Польова")
                .addArrivalMinutes("43 хв")
                .addDateType(0)
                .addLowFloor(false)
                .build();
        assertEquals(packet, request.toString());
    }
}