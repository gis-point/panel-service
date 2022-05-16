package com.microgis.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StopNameResponse extends Response {

    private String stopName;

    public StopNameResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.stopName = indexInBound(data, 2);
    }
}
