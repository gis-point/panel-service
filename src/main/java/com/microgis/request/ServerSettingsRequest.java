package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class ServerSettingsRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SERVER_SETTINGS);
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
        public ServerSettingsRequest build() {
            return new ServerSettingsRequest(this);
        }
    }

    private ServerSettingsRequest(Builder builder) {
        super(builder);
    }
}
