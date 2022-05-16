package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class TimeFieldTextRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_TEXT_FOR_TIME_FIELD);
        }

        public Builder addNumber(String number) {
            getBody().append("$");
            getBody().append(number);
            return self();
        }

        public Builder addText(String text) {
            getBody().append("$");
            getBody().append(text);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public TimeFieldTextRequest build() {
            return new TimeFieldTextRequest(this);
        }
    }

    private TimeFieldTextRequest(Builder builder) {
        super(builder);
    }
}
