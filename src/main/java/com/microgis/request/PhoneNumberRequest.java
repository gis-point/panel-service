package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class PhoneNumberRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_PHONE_NUMBER);
        }

        public Builder addNumber(int number) {
            getBody().append("$");
            getBody().append("PHONE").append(number);
            return self();
        }

        public Builder addPhoneNumber(String phoneNumber) {
            getBody().append("$");
            getBody().append(phoneNumber).append("$");
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PhoneNumberRequest build() {
            return new PhoneNumberRequest(this);
        }
    }

    private PhoneNumberRequest(Builder builder) {
        super(builder);
    }
}
