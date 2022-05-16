package com.microgis.controller.dto.panel;

import java.util.HashMap;
import java.util.Map;

public enum Command {
    TICKET(1),
    IDENTIFICATION(2),
    ROUTE_LINE(3),
    SET_COMPLEX_COMMUNICATION_PARAMETERS(4),
    RELOAD(5),
    SET_PHONE_NUMBER(7),
    RESET_ALL_SETTINGS(8),
    RUNNING_LINE_TEXT(10),
    RESET_TICKER(11),
    SET_TEXT_FOR_TIME_FIELD(14),
    READ_TEXT_FOR_TIME_FIELD(15),
    STATIC_TEXT(16),
    RESET_STATIC_TEXT(17),
    SYNC_PACKAGE(20),
    ANSWER_SYNC_PACKAGE(21),
    SET_GPRS(50),
    READ_GPRS(51),
    SERVER_SETTINGS(52),
    GET_SERVER_SETTINGS(53),
    SET_STOP_NAME(60),
    READ_STOP_NAME(61),
    SET_PHYSICAL_INFO(302),
    READ_PHYSICAL_INFO(303),
    SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS(304),
    READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS(305),
    SET_DISPLAY_STYLE_PARAMETERS(306),
    READ_DISPLAY_STYLE_PARAMETERS(307),
    SET_OPERATION_PARAMETERS(310),
    READ_OPERATION_PARAMETERS(311),
    SET_TIME_ZONE(320),
    READ_TIME_ZONE(321),
    SET_ADDITIONAL_SERVER(400),
    READ_ADDITIONAL_SERVER(401),
    SET_ADDITIONAL_SERVER_TIMEOUT(402),
    SET_LOGICAL_NUMBER(404),
    READ_LOGICAL_NUMBER(405),
    RESTART_MODEM(555),
    RESETTING_COMMUNICATION_PARAMETERS(558);

    private static Map<Integer, Command> commands;

    private final int number;

    Command(int p) {
        number = p;
    }

    public int getCommandNumber() {
        return number;
    }

    public static Command getCommand(Integer i) {
        if (commands == null) {
            initMapping();
        }
        return commands.get(i);
    }

    private static void initMapping() {
        commands = new HashMap<>();
        for (Command errorType : values()) {
            commands.put(errorType.number, errorType);
        }
    }
}
