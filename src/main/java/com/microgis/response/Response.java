package com.microgis.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microgis.controller.dto.panel.Command;
import com.microgis.util.Constants;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.regex.Pattern;

@ToString
@EqualsAndHashCode
public class Response {

    @Setter
    private int panelId;

    private String[] data;

    private Command command;

    public Response() {
    }

    public Response(StringBuilder packet) {
        packet.replace(0, Constants.START.length(), "");
        packet.replace(packet.length() - Constants.END.length(), packet.length(), "");
        data = packet.toString().split(Pattern.quote("$"));
        command = Command.getCommand(Integer.parseInt(data[0]));
    }

    @SuppressWarnings("CopyConstructorMissesField")
    public Response(Response response) {
        this.data = response.data;
        this.command = response.getCommand();
    }

    public static String indexInBound(String[] data, int index) {
        return (data != null && index >= 0 && index < data.length) ? data[index] : null;
    }

    protected boolean convertStringToBoolean(String result) {
        if ("0".equals(result)) {
            return false;
        } else if ("1".equals(result)) {
            return true;
        }
        throw new NumberFormatException("Can't convert string to boolean");
    }

    @JsonIgnore
    public String[] getData() {
        return data;
    }

    @JsonIgnore
    public Command getCommand() {
        return command;
    }

    @JsonIgnore
    public int getPanelId() {
        return panelId;
    }
}