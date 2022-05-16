package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class AdjustPhysicalInfoRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_PHYSICAL_INFO);
        }

        public Builder addTextSpeed(int textSpeed) {
            getBody().append("$");
            getBody().append(textSpeed);
            return self();
        }

        public Builder addMaxBrightness(int maxBrightness) {
            getBody().append("$");
            getBody().append(maxBrightness);
            return self();
        }

        public Builder addMinBrightness(int minBrightness) {
            getBody().append("$");
            getBody().append(minBrightness);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public AdjustPhysicalInfoRequest build() {
            return new AdjustPhysicalInfoRequest(this);
        }
    }

    private AdjustPhysicalInfoRequest(Builder builder) {
        super(builder);
    }
}
