package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class DisplayStyleParametersRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_DISPLAY_STYLE_PARAMETERS);
        }

        public Builder addOutputControlBits(String outputControlBits) {
            getBody().append("$");
            getBody().append(outputControlBits);
            return self();
        }

        public Builder addProhibitionToDisplayTime(String prohibitionToDisplayTime) {
            getBody().append("$");
            getBody().append(prohibitionToDisplayTime);
            return self();
        }

        public Builder addLineOutputMode(String lineOutputMode) {
            getBody().append("$");
            getBody().append(lineOutputMode);
            return self();
        }

        public Builder addDisplayModeTime(String displayModeTime) {
            getBody().append("$");
            getBody().append(displayModeTime);
            return self();
        }

        public Builder addArrivalForecastAccuracy(String arrivalForecastAccuracy) {
            getBody().append("$");
            getBody().append(arrivalForecastAccuracy);
            return self();
        }

        public Builder addMaxValueArrival(String maxValueArrival) {
            getBody().append("$");
            getBody().append(maxValueArrival);
            return self();
        }

        public Builder addForecastAccuracy(String forecastAccuracy) {
            getBody().append("$");
            getBody().append(forecastAccuracy);
            return self();
        }

        public Builder addSwitchingInterval(String switchingInterval) {
            getBody().append("$");
            getBody().append(switchingInterval);
            return self();
        }

        public Builder addReadIntervals(String readInterval) {
            getBody().append("$");
            getBody().append(readInterval);
            return self();
        }

        public Builder addConsiderQuantityLines(String considerQuantityLines) {
            getBody().append("$");
            getBody().append(considerQuantityLines);
            return self();
        }

        public Builder addMaxDisplayTime(String maxDisplayTime) {
            getBody().append("$");
            getBody().append(maxDisplayTime);
            return self();
        }

        public Builder addMinDisplayTime(String minDisplayTime) {
            getBody().append("$");
            getBody().append(minDisplayTime);
            return self();
        }

        public Builder addExpirationTime(String expirationTime) {
            getBody().append("$");
            getBody().append(expirationTime);
            return self();
        }

        public Builder addIntervalBetweenConnectionsServer(String intervalBetweenConnectionsServer) {
            getBody().append("$");
            getBody().append(intervalBetweenConnectionsServer);
            return self();
        }

        public Builder addWitBeforeBitOutput(String waitBeforeBitOutput) {
            getBody().append("$");
            getBody().append(waitBeforeBitOutput);
            return self();
        }

        public Builder addInformationVerticalLines(String informationVerticalLines) {
            getBody().append("$");
            getBody().append(informationVerticalLines);
            return self();
        }

        public Builder addInformationHeightLinesDots(String informationHeightLinesDots) {
            getBody().append("$");
            getBody().append(informationHeightLinesDots);
            return self();
        }

        public Builder addStartLineHeight(String startLineHeight) {
            getBody().append("$");
            getBody().append(startLineHeight);
            return self();
        }

        public Builder addScheduleLineHeight(String scheduleLineHeight) {
            getBody().append("$");
            getBody().append(scheduleLineHeight);
            return self();
        }

        public Builder addConstantBitFlags(String constantBitFlags) {
            getBody().append("$");
            getBody().append(constantBitFlags);
            return self();
        }

        public Builder addHorizontalLocationTime(String horizontalLocationTime) {
            getBody().append("$");
            getBody().append(horizontalLocationTime);
            return self();
        }

        public Builder addVerticalLocationTime(String verticalLocationTime) {
            getBody().append("$");
            getBody().append(verticalLocationTime);
            return self();
        }

        public Builder addFontIndexOutput(String fontIndexOutput) {
            getBody().append("$");
            getBody().append(fontIndexOutput);
            return self();
        }

        public Builder addConstantBitFlagsDisplaying(String constantBitFlagsDisplaying) {
            getBody().append("$");
            getBody().append(constantBitFlagsDisplaying);
            return self();
        }

        public Builder addHorizontalCoordinateDate(String horizontalCoordinateDate) {
            getBody().append("$");
            getBody().append(horizontalCoordinateDate);
            return self();
        }

        public Builder addVerticalCoordinateDate(String verticalCoordinateDate) {
            getBody().append("$");
            getBody().append(verticalCoordinateDate);
            return self();
        }

        public Builder addFontIndexOutputDates0(String fontIndexOutputDates0) {
            getBody().append("$");
            getBody().append(fontIndexOutputDates0);
            return self();
        }

        public Builder addDateDisplayLanguage0(String dateDisplayLanguage0) {
            getBody().append("$");
            getBody().append(dateDisplayLanguage0);
            return self();
        }

        public Builder addCaseTextOutput0(String caseTextOutput0) {
            getBody().append("$");
            getBody().append(caseTextOutput0);
            return self();
        }

        public Builder addConstantBitFlagsDisplayingFirstDate(String constantBitFlagsDisplayingFirstDate) {
            getBody().append("$");
            getBody().append(constantBitFlagsDisplayingFirstDate);
            return self();
        }

        public Builder addHorizontalLocationDate(String horizontalLocationDate) {
            getBody().append("$");
            getBody().append(horizontalLocationDate);
            return self();
        }

        public Builder addVerticalLocationDate(String verticalLocationDate) {
            getBody().append("$");
            getBody().append(verticalLocationDate);
            return self();
        }

        public Builder addFontIndexOutputDates1(String fontIndexOutputDates1) {
            getBody().append("$");
            getBody().append(fontIndexOutputDates1);
            return self();
        }

        public Builder addDateDisplayLanguage1(String dateDisplayLanguage1) {
            getBody().append("$");
            getBody().append(dateDisplayLanguage1);
            return self();
        }

        public Builder addCaseTextOutput1(String caseTextOutput1) {
            getBody().append("$");
            getBody().append(caseTextOutput1);
            return self();
        }

        public Builder addDistanceBetweenPoints(String distanceBetweenPoints) {
            getBody().append("$");
            getBody().append(distanceBetweenPoints);
            return self();
        }

        public Builder addNumberBetweenSpaces(String numberBetweenSpaces) {
            getBody().append("$");
            getBody().append(numberBetweenSpaces);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DisplayStyleParametersRequest build() {
            return new DisplayStyleParametersRequest(this);
        }
    }

    private DisplayStyleParametersRequest(Builder builder) {
        super(builder);
    }
}
