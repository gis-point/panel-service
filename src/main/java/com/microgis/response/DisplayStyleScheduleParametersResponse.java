package com.microgis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DisplayStyleScheduleParametersResponse extends Response {

    /**
     * Минимальное начало
     * названия остановки
     */
    @NotNull
    private Integer minStopName;

    /**
     * Правый конец поля
     * названия
     */
    @NotNull
    private Integer rightEndField;

    /**
     * Минимальный отступ
     * между номером и
     * названием конечной
     */
    @NotNull
    private Integer minIndent;

    /**
     * Начало пиктограммы
     * низкопольного ТС
     */
    @NotNull
    private Integer startPictogram;

    /**
     * Пропуск перед
     * пиктограммой, когда
     * она в поле конечной
     */
    @NotNull
    private Integer skipBeforePictogram;

    /**
     * Начало времени
     */
    @NotNull
    private Integer startTime;

    /**
     * Ширина поля времени
     * (одного или двух)
     */
    @NotNull
    private Integer fieldTimeWidth;

    /**
     * Расстояние между
     * двумя полями
     * времен
     */
    @NotNull
    private Integer distanceBetweenTimesFields;

    /**
     * Индекс шрифта Для
     * номера маршрута и
     * конечной
     */
    @NotNull
    private Integer fontIndexForRouteAndUltimate;

    /**
     * Индекс шрифта Для
     * значения интервала
     * времени до
     * прибытия ТС
     */
    @NotNull
    private Integer fontIndexForTimeAndArrived;

    /**
     * Индекс шрифта Для конечной
     */
    @NotNull
    private Integer fontIndexForUltimate;

    /**
     * Индекс шрифта Для
     * Бегущей строки
     */
    @NotNull
    private Integer fontIndexForTicker;

    /**
     * Межсимвольный
     * интервал конечной
     */
    @NotNull
    private Integer interSymbolInterval;

    /**
     * Ширина пробела
     * конечной
     */
    @NotNull
    private Integer spaceWidthUltimate;

    /**
     * Межсимвольный
     * интервал времени
     */
    @NotNull
    private Integer interSymbolTimeInterval;

    /**
     * Ширина пробела
     * времени
     */
    @NotNull
    private Integer spaceWidthTime;

    /**
     * Выравнивание
     * времени
     */
    @NotNull
    private Integer alignmentTime;

    /**
     * Символ разделитель
     * надписей на разных
     * языках для всего
     * табло
     */
    @NotNull
    private Integer separatorCharacterInscriptions;

    /**
     * Кол-во языков, кот
     * передаются в
     * названиях конечных
     * остановок
     */
    @NotNull
    private Integer languageCount;

    /**
     * Биты управления
     * выводом
     */
    @NotNull
    private Integer controlBitsConclusion;

    /**
     * Длительность  отображения второго
     * времени прибытия (в
     * секундах) или 0
     */
    @NotNull
    private Integer durationOutputSecondArrival;

    /**
     * Длительность отображения
     * пиктограммы низкопольного
     * транспорта в поле
     * второго времени
     * прибытия (в секундах)
     */
    @NotNull
    private Integer durationShowPictogram;

    @SuppressWarnings("java:S2637")
    public DisplayStyleScheduleParametersResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.minStopName = Integer.parseInt(indexInBound(data, 2));
        this.rightEndField = Integer.parseInt(indexInBound(data, 3));
        this.minIndent = Integer.parseInt(indexInBound(data, 4));
        this.startPictogram = Integer.parseInt(indexInBound(data, 5));
        this.skipBeforePictogram = Integer.parseInt(indexInBound(data, 6));
        this.startTime = Integer.parseInt(indexInBound(data, 7));
        this.fieldTimeWidth = Integer.parseInt(indexInBound(data, 8));
        this.distanceBetweenTimesFields = Integer.parseInt(indexInBound(data, 9));
        this.fontIndexForRouteAndUltimate = Integer.parseInt(indexInBound(data, 10));
        this.fontIndexForTimeAndArrived = Integer.parseInt(indexInBound(data, 11));
        this.fontIndexForUltimate = Integer.parseInt(indexInBound(data, 12));
        this.fontIndexForTicker = Integer.parseInt(indexInBound(data, 13));
        this.interSymbolInterval = Integer.parseInt(indexInBound(data, 14));
        this.spaceWidthUltimate = Integer.parseInt(indexInBound(data, 15));
        this.interSymbolTimeInterval = Integer.parseInt(indexInBound(data, 16));
        this.spaceWidthTime = Integer.parseInt(indexInBound(data, 17));
        this.alignmentTime = Integer.parseInt(indexInBound(data, 18));
        this.separatorCharacterInscriptions = Integer.parseInt(indexInBound(data, 19));
        this.languageCount = Integer.parseInt(indexInBound(data, 20));
        this.controlBitsConclusion = Integer.parseInt(indexInBound(data, 21));
        this.durationOutputSecondArrival = Integer.parseInt(indexInBound(data, 22));
        this.durationShowPictogram = Integer.parseInt(indexInBound(data, 23));
    }
}
