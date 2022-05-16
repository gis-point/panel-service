package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class RunningLineTextRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.RUNNING_LINE_TEXT);
        }

        public Builder addTextPart(int part) {
            getBody().append("$");
            getBody().append(part);
            return self();
        }

        public Builder addTextPartCount(int count) {
            getBody().append("$");
            getBody().append(count);
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
        public RunningLineTextRequest build() {
            return new RunningLineTextRequest(this);
        }
    }

    private RunningLineTextRequest(Builder builder) {
        super(builder);
    }
}
