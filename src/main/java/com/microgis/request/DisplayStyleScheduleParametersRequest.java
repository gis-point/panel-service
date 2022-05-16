package com.microgis.request;

import com.microgis.controller.dto.panel.Command;

public class DisplayStyleScheduleParametersRequest extends Request {

    public static class Builder extends Request.Builder<Builder> {

        public Builder() {
            addCommand(Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS);
        }

        public Builder addMinStopName(String minStopName) {
            getBody().append("$");
            getBody().append(minStopName);
            return self();
        }

        public Builder addRightEndField(String rightEndField) {
            getBody().append("$");
            getBody().append(rightEndField);
            return self();
        }

        public Builder addMinIndent(String minIndent) {
            getBody().append("$");
            getBody().append(minIndent);
            return self();
        }

        public Builder addStartPictogram(String startPictogram) {
            getBody().append("$");
            getBody().append(startPictogram);
            return self();
        }

        public Builder addSkipBeforePictogram(String skipBeforePictogram) {
            getBody().append("$");
            getBody().append(skipBeforePictogram);
            return self();
        }

        public Builder addStartTime(String startTime) {
            getBody().append("$");
            getBody().append(startTime);
            return self();
        }

        public Builder addFieldTimeWidth(String fieldTimeWidth) {
            getBody().append("$");
            getBody().append(fieldTimeWidth);
            return self();
        }

        public Builder addDistanceBetweenTimesFieldsh(String distanceBetweenTimesFields) {
            getBody().append("$");
            getBody().append(distanceBetweenTimesFields);
            return self();
        }

        public Builder addFontIndexForRouteAndUltimate(
                String fontIndexForRouteAndUltimate) {
            getBody().append("$");
            getBody().append(fontIndexForRouteAndUltimate);
            return self();
        }

        public Builder addFontIndexForTimeAndArrived(String fontIndexForTimeAndArrived) {
            getBody().append("$");
            getBody().append(fontIndexForTimeAndArrived);
            return self();
        }

        public Builder addFontIndexForUltimate(String fontIndexForUltimate) {
            getBody().append("$");
            getBody().append(fontIndexForUltimate);
            return self();
        }

        public Builder addFontIndexForTicker(String fontIndexForTicker) {
            getBody().append("$");
            getBody().append(fontIndexForTicker);
            return self();
        }

        public Builder addInterSymbolInterval(String interSymbolInterval) {
            getBody().append("$");
            getBody().append(interSymbolInterval);
            return self();
        }

        public Builder addSpaceWidthUltimate(String spaceWidthUltimate) {
            getBody().append("$");
            getBody().append(spaceWidthUltimate);
            return self();
        }

        public Builder addInterSymbolTimeInterval(String interSymbolTimeInterval) {
            getBody().append("$");
            getBody().append(interSymbolTimeInterval);
            return self();
        }

        public Builder addSpaceWidthTime(String spaceWidthTime) {
            getBody().append("$");
            getBody().append(spaceWidthTime);
            return self();
        }

        public Builder addAlignmentTime(String alignmentTime) {
            getBody().append("$");
            getBody().append(alignmentTime);
            return self();
        }

        public Builder addSeparatorCharacterInscriptions(
                String separatorCharacterInscriptions) {
            getBody().append("$");
            getBody().append(separatorCharacterInscriptions);
            return self();
        }

        public Builder addLanguageCount(String languageCount) {
            getBody().append("$");
            getBody().append(languageCount);
            return self();
        }

        public Builder addControlBitsConclusion(String controlBitsConclusion) {
            getBody().append("$");
            getBody().append(controlBitsConclusion);
            return self();
        }

        public Builder addDurationOutputSecondArrival(
                String durationOutputSecondArrival) {
            getBody().append("$");
            getBody().append(durationOutputSecondArrival);
            return self();
        }

        public Builder addDurationShowPictogram(String durationShowPictogram) {
            getBody().append("$");
            getBody().append(durationShowPictogram);
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public DisplayStyleScheduleParametersRequest build() {
            return new DisplayStyleScheduleParametersRequest(this);
        }
    }

    private DisplayStyleScheduleParametersRequest(Builder builder) {
        super(builder);
    }
}
