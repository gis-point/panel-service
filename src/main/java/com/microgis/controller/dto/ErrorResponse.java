package com.microgis.controller.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ErrorResponse {

    private int httpStatus;

    private String message;

    private String field;

}