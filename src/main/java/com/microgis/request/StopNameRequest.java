package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class StopNameRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_STOP_NAME);
        }

        public Builder addStopName(String stopName) {
            getBody().append("$");
            getBody().append(stopName);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public StopNameRequest build() {
            return new StopNameRequest(this);
        }
    }

    private StopNameRequest(Builder builder) {
        super(builder);
    }
}
