package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RouteLineRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        public Builder() {
            addCommand(Command.ROUTE_LINE);
        }

        public Builder addDate(Date date) {
            getBody().append("$");
            getBody().append(formatter.format(Objects.requireNonNull(date)));
            return self();
        }

        public Builder addScheduleLineNumber(int scheduleNumbers) {
            getBody().append("$");
            getBody().append(scheduleNumbers);
            return self();
        }

        public Builder addDeviceType(String deviceType) {
            getBody().append("$");
            getBody().append(Objects.requireNonNull(deviceType));
            return self();
        }

        public Builder addRouteNumber(String routeNumber) {
            getBody().append("|");
            getBody().append(Objects.requireNonNull(routeNumber));
            return self();
        }

        public Builder addRouteName(String routeName) {
            getBody().append("|");
            getBody().append(Objects.requireNonNull(routeName));
            return self();
        }

        public Builder addArrivalMinutes(String arrivalMin) {
            getBody().append("|");
            getBody().append(arrivalMin);
            return self();
        }

        public Builder addDateType(int dateType) {
            getBody().append("|");
            getBody().append(dateType);
            return self();
        }

        public Builder addLowFloor(boolean lowFloor) {
            getBody().append("|");
            getBody().append(lowFloor ? "1" : "0");
            getBody().append("|");
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public RouteLineRequest build() {
            return new RouteLineRequest(this);
        }
    }

    private RouteLineRequest(Builder builder) {
        super(builder);
    }
}
