package com.microgis.response;

import com.microgis.controller.dto.panel.ErrorType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class TicketResponse extends Response {

    private int logicalNumber;

    private double panelVersion;

    private int scheduleLineCount;

    private int bufferSize;

    private ErrorType errorType;

    private int maxRoutCount;

    private int maxEndPointNameLength; //default = 96

    private int viewSate;

    private String imei;

    private int projectCode;

    private int physicalPanelNumber;

    public TicketResponse(StringBuilder packet) {
        super(packet);
        parse();
    }

    public TicketResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        logicalNumber = Integer.parseInt(indexInBound(data, 2));
        panelVersion = Double.parseDouble(indexInBound(data, 3));
        scheduleLineCount = Integer.parseInt(indexInBound(data, 4));
        bufferSize = Integer.parseInt(indexInBound(data, 5));
        errorType = ErrorType.getError(Integer.parseInt(indexInBound(data, 6).split("\\(")[0]));
        maxRoutCount = Integer.parseInt(indexInBound(data, 7));
        maxEndPointNameLength = Integer.parseInt(indexInBound(data, 8));
        viewSate = Integer.parseInt(indexInBound(data, 9));
        imei = indexInBound(data, 10);
        projectCode = Integer.parseInt(indexInBound(data, 11));
        physicalPanelNumber = Integer.parseInt(indexInBound(data, 12));
    }
}