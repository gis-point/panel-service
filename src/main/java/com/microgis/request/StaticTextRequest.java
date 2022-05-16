package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class StaticTextRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.STATIC_TEXT);
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
        public StaticTextRequest build() {
            return new StaticTextRequest(this);
        }
    }

    private StaticTextRequest(Builder builder) {
        super(builder);
    }
}
