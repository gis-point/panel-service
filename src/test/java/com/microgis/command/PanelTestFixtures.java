package com.microgis.command;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microgis.controller.dto.GtfsResponse;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.controller.dto.prediction.StopInfo;
import com.microgis.controller.dto.sms.SmsRequest;
import com.microgis.controller.dto.sms.SmsResponse;
import com.microgis.document.Panels;

import java.io.File;
import java.util.List;

public class PanelTestFixtures {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    public static StopInfo createPredictionResponse() {
        return readValue("src/test/resources/response/prediction.json", StopInfo.class);
    }

    public static StopInfo createPredictionResponseZero() {
        return readValue("src/test/resources/response/predictionZero.json", StopInfo.class);
    }

    public static SmsResponse createSmsResponse() {
        return readValue("src/test/resources/response/smsResponse.json", SmsResponse.class);
    }

    public static SmsResponse createSmsResponseError() {
        return readValue("src/test/resources/response/smsResponseError.json", SmsResponse.class);
    }

    public static GtfsResponse createGtfsResponse() {
        return readValue("src/test/resources/response/gtfs.json", GtfsResponse.class);
    }

    public static List<Panels> createPanels() {
        return readValueList("src/test/resources/response/panels.json",
                new TypeReference<List<Panels>>() {
                });
    }

    public static SmsRequest createSmsRequest() {
        SmsRequest smsRequest = new SmsRequest();
        smsRequest.setDestination("380606301237");
        smsRequest.setContent("text");
        return smsRequest;
    }

    public static Panel createPanel() {
        Panel panel = new Panel();
        panel.setPhysicalNumber(4997);
        panel.setLogicalNumber(4997);
        panel.setStopId(4997);
        panel.setIdentified(true);
        return panel;
    }

    private static <T> T readValue(String path, Class<T> valueType) {
        try {
            return objectMapper.readValue(new File(path), valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T readValueList(String path, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(new File(path), valueTypeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}