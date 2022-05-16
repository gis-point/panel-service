package com.microgis.controller.dto.panel;

import java.util.HashMap;
import java.util.Map;

public enum ErrorType {
    NONE(0, "Нет ошибки"),
    WRONG_MESSAGE_FORMAT(1, "Неверный формат сообщения"),
    WRONG_ROUTE_LINE_NUMBER(2, "Неверный номер строки маршрута"),
    TOO_BIG_TEXT_SIZE(3, "Слишком большой размер текста"),
    FIRST_TEXT_LINE_WAS_REJECTED(4, "Не была принята первая часть текста бегущей строки"),
    WRONG_CREEPING_LINE_PART_SEQUENCE(5, "Нарушена последовательная нумерация частей бегущей строки"),
    UNKNOWN_PARAMETER(6, "Неизвестное имя параметра табло"),
    WRONG_PHONE_CELL_NUMBER(7, "Неверный порядковый номер ячейки для записи разрешенных номеров телефонов"),
    TOO_LONG_PHONE_NUMBER(8, "Слишком длинный номер телефона"),
    UNKNOWN_COMMAND(9, "Неизвестная Команда"),
    TOO_LONG_DATA_TEXT_LENGTH(10, "Слишком длинная текстовая строка данных"),
    WRONG_DATE_TIME_FORMAT(11, "Неверный формат времени и даты");

    private static Map<Integer, ErrorType> valueToError;
    private final int number;
    private final String text;

    ErrorType(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public static ErrorType getError(Integer i) {
        if (valueToError == null) {
            initMapping();
        }
        return valueToError.get(i);
    }

    private static void initMapping() {
        valueToError = new HashMap<>();
        for (ErrorType errorType : values()) {
            valueToError.put(errorType.number, errorType);
        }
    }

    public String getText() {
        return text;
    }
}
