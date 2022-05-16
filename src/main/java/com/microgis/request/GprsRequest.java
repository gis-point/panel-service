package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class GprsRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_GPRS);
        }

        public Builder addAddress(String address) {
            getBody().append("$");
            getBody().append(address);
            return self();
        }

        public Builder addLogin(String login) {
            getBody().append("$");
            getBody().append(login);
            return self();
        }

        public Builder addPassword(String password) {
            getBody().append("$");
            getBody().append(password);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public GprsRequest build() {
            return new GprsRequest(this);
        }
    }

    private GprsRequest(Builder builder) {
        super(builder);
    }
}
