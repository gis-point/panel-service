package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class ComplexCommunicationParametersRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_COMPLEX_COMMUNICATION_PARAMETERS);
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

        public Builder addServerAddress(String serverAddress) {
            getBody().append("$");
            getBody().append(serverAddress);
            return self();
        }

        public Builder addServerPort(String serverPort) {
            getBody().append("$");
            getBody().append(serverPort);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ComplexCommunicationParametersRequest build() {
            return new ComplexCommunicationParametersRequest(this);
        }
    }

    private ComplexCommunicationParametersRequest(Builder builder) {
        super(builder);
    }
}
