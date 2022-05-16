package com.microgis.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdditionalServerSettingsResponse extends Response {

    private String ip;

    @Min(value = 1, message = "The value '${validatedValue}' should be from 1 to 65535")
    @Max(value = 65535, message = "The value '${validatedValue}' should be from 1 to 65535")
    private int port;

    public AdditionalServerSettingsResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.ip = indexInBound(data, 2);
        this.port = Integer.parseInt(indexInBound(data, 3));
    }
}
