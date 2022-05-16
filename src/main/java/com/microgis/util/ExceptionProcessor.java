package com.microgis.util;

import com.microgis.controller.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionProcessor.class);

    private static final String ERROR_MESSAGE = "Error occurred {}";

    @ExceptionHandler({Exception.class, IOException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(Throwable e) {
        e = unwrapLastThrownException(e);
        LOGGER.error("Error occurred {}, {}", e.getMessage(), e);
        LOGGER.error("StackTrace --{}", Arrays.stream(e.getStackTrace())
                .map(Objects::toString)
                .collect(Collectors.joining("\n")));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementException e) {
        LOGGER.debug(ERROR_MESSAGE, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({NumberFormatException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException e) {
        LOGGER.debug(ERROR_MESSAGE, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({ArrayIndexOutOfBoundsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException e) {
        LOGGER.debug(ERROR_MESSAGE, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({WebExchangeBindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        LOGGER.debug(ERROR_MESSAGE, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getAllErrors().get(0).getDefaultMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.debug(ERROR_MESSAGE, e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.debug(ERROR_MESSAGE, e.getMessage());
        AtomicReference<String> field = new AtomicReference<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> field.set(fieldError.getField()));
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), field.get());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private Throwable unwrapLastThrownException(Throwable e) {
        return Optional.ofNullable(e.getCause()).orElse(e);
    }

}
