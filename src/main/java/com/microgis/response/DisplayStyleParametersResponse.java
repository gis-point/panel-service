package com.microgis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DisplayStyleParametersResponse extends Response {

    /**
     * Биты управления выводом
     * информационной строки
     */
    @NotNull
    private Integer outputControlBits;

    /**
     * Запрет выводить время
     * прибытия, если оно уже
     * прошло
     */
    @NotNull
    private Integer prohibitionToDisplayTime;

    /**
     * Режим вывода строк расписания
     */
    @Min(0)
    @Max(1)
    @NotNull
    private Integer lineOutputMode;

    /**
     * Режим отображения времени
     */
    @Min(0)
    @Max(1)
    @NotNull
    private Integer displayModeTime;

    /**
     * Точность прогноза прибытия
     * в минутах
     */
    @NotNull
    private Integer arrivalForecastAccuracy;

    /**
     * Максимальное значение
     * интервала времени
     * прибытия
     */
    @NotNull
    private Integer maxValueArrival;

    /**
     * Точность прогноза при, если
     * время прошло (в минутах)
     */
    @NotNull
    private Integer forecastAccuracy;

    /**
     * Интервал переключения
     * надписей в информационной
     * строке (секунды)
     */
    @NotNull
    private Integer switchingInterval;

    /**
     * Интервал на прочтение
     * одной строк маршрута в
     * секундах
     */
    @NotNull
    private Integer readInterval;

    /**
     * Учитывать ли количество
     * строк при отображении
     * страницами
     */
    @Min(0)
    @Max(1)
    @NotNull
    private Integer considerQuantityLines;

    /**
     * Максимальное время показа
     * страницы маршрутов
     */
    @NotNull
    private Integer maxDisplayTime;

    /**
     * Минимальное время показа
     * страницы маршрутов
     */
    @NotNull
    private Integer minDisplayTime;

    /**
     * 0 или время устаревания
     * информации о маршруте в
     * минутах
     */
    @NotNull
    private Integer expirationTime;

    /**
     * Интервал в секундах между
     * подключениями к серверу
     */
    @NotNull
    private Integer intervalBetweenConnectionsServer;

    /**
     * Сколько минут ждать перед
     * выводом битовой
     * диагностики на экран табло
     * в случае отсутствия связи
     */
    @NotNull
    private Integer waitBeforeBitOutput;

    /**
     * Смещение информационной
     * по вертикали строки
     * относительно верхнего края
     * табло (в точках)
     */
    @NotNull
    private Integer informationVerticalLines;

    /**
     * Высота информационной
     * строки в точках
     */
    @NotNull
    private Integer informationHeightLinesDots;

    /**
     * Начало по высоте строк
     * расписания в точках
     */
    @NotNull
    private Integer startLineHeight;

    /**
     * Высота строки расписания в
     * точках
     */
    @NotNull
    private Integer scheduleLineHeight;

    /**
     * Битовые флаги постоянного
     */
    @NotNull
    private Integer constantBitFlags;

    /**
     * Горизонтальная координата
     * расположения времени (в
     * точках)
     */
    @NotNull
    private Integer horizontalLocationTime;

    /**
     * Вертикальная координата
     * расположения времени (в
     * точках)
     */
    @NotNull
    private Integer verticalLocationTime;

    /**
     * Индекс шрифта для вывода
     * времени
     */
    @NotNull
    private Integer fontIndexOutput;

    /**
     * Битовые флаги постоянного
     * вывода первой даты 0 в
     * соответствии с протоколом
     * управления световым табло
     */
    @NotNull
    private Integer constantBitFlagsDisplaying;

    /**
     * Горизонтальная координата
     * расположения даты 0 (в
     * точках)
     */
    @NotNull
    private Integer horizontalCoordinateDate;

    /**
     * Вертикальная координата
     * расположения даты 0 (в
     * точках)
     */
    @NotNull
    private Integer verticalCoordinateDate;

    /**
     * Индекс шрифта для вывода
     * даты 0
     */
    @NotNull
    private Integer fontIndexOutputDates0;

    /**
     * Язык отображения даты 0
     */
    @Min(0)
    @Max(1)
    @NotNull
    private Integer dateDisplayLanguage0;

    /**
     * Регистр вывода текста в
     * дате 0
     */
    @Min(0)
    @Max(2)
    @NotNull
    private Integer caseTextOutput0;

    /**
     * Битовые флаги постоянного
     * вывода первой даты 1 в
     * соответствии с протоколом
     * управления световым табло
     */
    @NotNull
    private Integer constantBitFlagsDisplayingFirstDate;

    /**
     * Горизонтальная координата
     * расположения даты 1 (в
     * точках)
     */
    @NotNull
    private Integer horizontalLocationDate;

    /**
     * Вертикальная координата
     * расположения даты 1 (в
     * точках)
     */
    @NotNull
    private Integer verticalLocationDate;

    /**
     * Индекс шрифта для вывода
     * даты 1
     */
    @NotNull
    private Integer fontIndexOutputDates1;

    /**
     * Язык отображения даты 1
     */
    @Min(0)
    @Max(1)
    @NotNull
    private Integer dateDisplayLanguage1;

    /**
     * Регистр вывода текста в
     * дате 1
     */
    @Min(0)
    @Max(2)
    @NotNull
    private Integer caseTextOutput1;

    /**
     * Расстояние в точках между
     * значением времени и другой
     * текстовой информацией в
     * информационной строке
     */
    @NotNull
    private Integer distanceBetweenPoints;

    /**
     * Количество пробелов между
     * значением даты и днем
     * недели при выводе в
     * информационной строке или
     * ноль для установки
     * табуляции между этими
     * данными
     */
    @NotNull
    private Integer numberBetweenSpaces;

    @SuppressWarnings("java:S2637")
    public DisplayStyleParametersResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.outputControlBits = Integer.parseInt(indexInBound(data, 2));
        this.prohibitionToDisplayTime = Integer.parseInt(indexInBound(data, 3));
        this.lineOutputMode = Integer.parseInt(indexInBound(data, 4));
        this.displayModeTime = Integer.parseInt(indexInBound(data, 5));
        this.arrivalForecastAccuracy = Integer.parseInt(indexInBound(data, 6));
        this.maxValueArrival = Integer.parseInt(indexInBound(data, 7));
        this.forecastAccuracy = Integer.parseInt(indexInBound(data, 8));
        this.switchingInterval = Integer.parseInt(indexInBound(data, 9));
        this.readInterval = Integer.parseInt(indexInBound(data, 10));
        this.considerQuantityLines = Integer.parseInt(indexInBound(data, 11));
        this.maxDisplayTime = Integer.parseInt(indexInBound(data, 12));
        this.minDisplayTime = Integer.parseInt(indexInBound(data, 13));
        this.expirationTime = Integer.parseInt(indexInBound(data, 14));
        this.intervalBetweenConnectionsServer = Integer.parseInt(indexInBound(data, 15));
        this.waitBeforeBitOutput = Integer.parseInt(indexInBound(data, 16));
        this.informationVerticalLines = Integer.parseInt(indexInBound(data, 17));
        this.informationHeightLinesDots = Integer.parseInt(indexInBound(data, 18));
        this.startLineHeight = Integer.parseInt(indexInBound(data, 19));
        this.scheduleLineHeight = Integer.parseInt(indexInBound(data, 20));
        this.constantBitFlags = Integer.parseInt(indexInBound(data, 21));
        this.horizontalLocationTime = Integer.parseInt(indexInBound(data, 22));
        this.verticalLocationTime = Integer.parseInt(indexInBound(data, 23));
        this.fontIndexOutput = Integer.parseInt(indexInBound(data, 24));
        this.constantBitFlagsDisplaying = Integer.parseInt(indexInBound(data, 25));
        this.horizontalCoordinateDate = Integer.parseInt(indexInBound(data, 26));
        this.verticalCoordinateDate = Integer.parseInt(indexInBound(data, 27));
        this.fontIndexOutputDates0 = Integer.parseInt(indexInBound(data, 28));
        this.dateDisplayLanguage0 = Integer.parseInt(indexInBound(data, 29));
        this.caseTextOutput0 = Integer.parseInt(indexInBound(data, 30));
        this.constantBitFlagsDisplayingFirstDate = Integer.parseInt(indexInBound(data, 31));
        this.horizontalLocationDate = Integer.parseInt(indexInBound(data, 32));
        this.verticalLocationDate = Integer.parseInt(indexInBound(data, 33));
        this.fontIndexOutputDates1 = Integer.parseInt(indexInBound(data, 34));
        this.dateDisplayLanguage1 = Integer.parseInt(indexInBound(data, 35));
        this.caseTextOutput1 = Integer.parseInt(indexInBound(data, 36));
        this.distanceBetweenPoints = Integer.parseInt(indexInBound(data, 37));
        this.numberBetweenSpaces = Integer.parseInt(indexInBound(data, 38));
    }
}