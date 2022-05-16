package com.microgis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ComplexCommunicationParametersResponse extends Response {

    /**
     * Адрес сервера
     * провайдера (APN)
     */
    @Size(max = 63, message = "The value '${validatedValue}' should be no longer than 63 symbols")
    @NotNull
    private String address;

    /**
     * Имя для доступа
     * (Login)
     */
    @Size(max = 20, message = "The value '${validatedValue}' should be no longer than 20 symbols")
    @NotNull
    private String login;

    /**
     * Пароль
     */
    @Size(max = 20, message = "The value '${validatedValue}' should be no longer than 20 symbols")
    @NotNull
    private String password;

    /**
     * Адрес сервера для
     * получения
     * информации о
     * маршрутах
     */
    @Size(max = 63, message = "The value '${validatedValue}' should be no longer than 63 symbols")
    @NotNull
    private String serverAddress;

    /**
     * Номер сетевого
     * порта на сервере
     * информации
     */
    @Min(value = 1, message = "The value '${validatedValue}' should be from 1 to 65535")
    @Max(value = 65535, message = "The value '${validatedValue}' should be from 1 to 65535")
    @NotNull
    private Integer serverPort;

}