package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class OperationParametersRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_OPERATION_PARAMETERS);
        }

        public Builder addSortMode(String sortMode) {
            getBody().append("$");
            getBody().append(sortMode);
            return self();
        }

        public Builder addServerTimeout(String serverTimeout) {
            getBody().append("$");
            getBody().append(serverTimeout);
            return self();
        }

        public Builder addKeepRouteRecordTime(String keepRouteRecordTime) {
            getBody().append("$");
            getBody().append(keepRouteRecordTime);
            return self();
        }

        public Builder addStartEveningTime(String startEveningTime) {
            getBody().append("$");
            getBody().append(startEveningTime);
            return self();
        }

        public Builder addFinishEveningTime(String finishEveningTime) {
            getBody().append("$");
            getBody().append(finishEveningTime);
            return self();
        }

        public Builder addAdditionalServerInterval(String additionalServerInterval) {
            getBody().append("$");
            getBody().append(additionalServerInterval);
            return self();
        }

        public Builder addAdditionalServerConnectionInterval(String additionalServerConnectionInterval) {
            getBody().append("$");
            getBody().append(additionalServerConnectionInterval);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public OperationParametersRequest build() {
            return new OperationParametersRequest(this);
        }
    }

    private OperationParametersRequest(Builder builder) {
        super(builder);
    }
}
