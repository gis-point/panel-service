package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class TimeZoneRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_TIME_ZONE);
        }

        public Builder addSummerTimeMode(String summerTimeMode) {
            getBody().append("$");
            getBody().append(summerTimeMode);
            return self();
        }

        public Builder addTimeZone(String timeZone) {
            getBody().append("$");
            getBody().append(timeZone);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public TimeZoneRequest build() {
            return new TimeZoneRequest(this);
        }
    }

    private TimeZoneRequest(Builder builder) {
        super(builder);
    }
}
