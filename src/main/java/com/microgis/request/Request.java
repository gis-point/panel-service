package com.microgis.request;

import com.microgis.controller.dto.panel.Command;
import com.microgis.util.Constants;

public abstract class Request {

    private final StringBuilder packet = new StringBuilder();

    protected Request() {
    }

    abstract static class Builder<T extends Builder<T>> {
        private final StringBuilder body = new StringBuilder();

        protected T addCommand(Command command) {
            body.append(command.getCommandNumber());
            return self();
        }

        public T addPacketNumber(int number) {
            body.append("$");
            body.append(number);
            return self();
        }

        abstract Request build();

        protected abstract T self();

        public StringBuilder getBody() {
            return body;
        }
    }

    Request(Builder<?> builder) {
        packet.append(Constants.START);
        packet.append(builder.body);
        packet.append(Constants.END);
    }

    @Override
    public String toString() {
        return packet.toString();
    }
}
