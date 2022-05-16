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
public class OperationParametersResponse extends Response {

    /**
     * Режим сортировки строк
     * расписания
     */
    @Min(value = 0, message = "The value '${validatedValue}' should be from 0 to 2")
    @Max(value = 2, message = "The value '${validatedValue}' should be from 0 to 2")
    @NotNull
    private Integer sortMode;

    /**
     * Сколько секунд ожидать
     * ответа от сервера
     */
    @NotNull
    private Integer serverTimeout;

    /**
     * Сколько часов хранить
     * запись маршрута для
     * отображения
     */
    @NotNull
    private Integer keepRouteRecordTime;

    /**
     * Начало вечернего времени с
     * точностью до часов
     */
    @NotNull
    private Integer startEveningTime;

    /**
     * Конец утреннего времени с
     * точностью до часов
     */
    @NotNull
    private Integer finishEveningTime;

    /**
     * Интервал между
     * подключениями к
     * альтернативному серверу
     * (секунды)
     */
    @NotNull
    private Integer additionalServerInterval;

    /**
     * Интервал времени, в
     * течение которого табло
     * остается подключенным к
     * альтернативному серверу
     * (секунды)
     */
    @NotNull
    private Integer additionalServerConnectionInterval;

    @SuppressWarnings("java:S2637")
    public OperationParametersResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.sortMode = Integer.parseInt(indexInBound(data, 2));
        this.serverTimeout = Integer.parseInt(indexInBound(data, 3));
        this.keepRouteRecordTime = Integer.parseInt(indexInBound(data, 4));
        this.startEveningTime = Integer.parseInt(indexInBound(data, 5));
        this.finishEveningTime = Integer.parseInt(indexInBound(data, 6));
        this.additionalServerInterval = Integer.parseInt(indexInBound(data, 7));
        this.additionalServerConnectionInterval = Integer.parseInt(indexInBound(data, 8));
    }
}
