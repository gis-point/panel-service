package com.microgis.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LogicalNumberResponse extends Response {

    private int currentNumber;

    private int physicalNumber;

    private int determinedNumber;

    public LogicalNumberResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.currentNumber = Integer.parseInt(indexInBound(data, 2));
        this.physicalNumber = Integer.parseInt(indexInBound(data, 3));
        this.determinedNumber = Integer.parseInt(indexInBound(data, 4));
    }
}
