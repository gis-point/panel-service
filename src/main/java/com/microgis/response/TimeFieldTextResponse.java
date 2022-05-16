package com.microgis.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class TimeFieldTextResponse extends Response {

    /**
     * Число от 0 до 2
     */
    @Min(value = 0, message = "The value '${validatedValue}' should be from 0 to 2")
    @Max(value = 2, message = "The value '${validatedValue}' should be from 0 to 2")
    private int number;

    /**
     * Текст надписи может содержать одновременно тексты на разных
     * языках. Эти тексты должны отделяться символом табуляции.
     * Максимальная длина текста — 48 символов.
     */
    @Size(max = 48, message = "The value '${validatedValue}' should be no longer than 48 symbols")
    private String text;

    public TimeFieldTextResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.number = Integer.parseInt(indexInBound(data, 2));
        this.text = indexInBound(data, 3);
    }
}
