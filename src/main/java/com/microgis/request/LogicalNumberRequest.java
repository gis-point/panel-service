package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class LogicalNumberRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_LOGICAL_NUMBER);
        }

        public Builder addLogicalNumber(long logicalNumber) {
            getBody().append("$");
            getBody().append(logicalNumber);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public LogicalNumberRequest build() {
            return new LogicalNumberRequest(this);
        }
    }

    private LogicalNumberRequest(Builder builder) {
        super(builder);
    }
}
