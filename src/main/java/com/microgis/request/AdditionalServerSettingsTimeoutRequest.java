package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class AdditionalServerSettingsTimeoutRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_ADDITIONAL_SERVER_TIMEOUT);
        }

        public Builder addTimeout(String ip) {
            getBody().append("$");
            getBody().append(ip);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public AdditionalServerSettingsTimeoutRequest build() {
            return new AdditionalServerSettingsTimeoutRequest(this);
        }
    }

    private AdditionalServerSettingsTimeoutRequest(Builder builder) {
        super(builder);
    }
}
