package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class IdentificationRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        public Builder() {
            addCommand(Command.IDENTIFICATION);
        }

        public Builder addDate(Date date) {
            getBody().append("$");
            getBody().append(formatter.format(Objects.requireNonNull(date)));
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public IdentificationRequest build() {
            return new IdentificationRequest(this);
        }
    }

    private IdentificationRequest(Builder builder) {
        super(builder);
    }
}
