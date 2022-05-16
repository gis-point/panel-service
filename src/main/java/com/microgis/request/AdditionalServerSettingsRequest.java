package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class AdditionalServerSettingsRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_ADDITIONAL_SERVER);
        }

        public Builder addIP(String ip) {
            getBody().append("$");
            getBody().append(ip);
            return self();
        }

        public Builder addPort(String port) {
            getBody().append("$");
            getBody().append(port);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public AdditionalServerSettingsRequest build() {
            return new AdditionalServerSettingsRequest(this);
        }
    }

    private AdditionalServerSettingsRequest(Builder builder) {
        super(builder);
    }
}
