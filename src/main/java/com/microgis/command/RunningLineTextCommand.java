package com.microgis.command;

import com.microgis.controller.dto.RouteResponse;
import com.microgis.controller.dto.RunningTextResponse;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.controller.dto.prediction.Dest;
import com.microgis.controller.dto.prediction.Pred;
import com.microgis.controller.dto.prediction.Prediction;
import com.microgis.request.RouteLineRequest;
import com.microgis.server.PanelHandler;
import com.microgis.util.Constants;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class RunningLineTextCommand extends Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunningLineTextCommand.class);

    private static final String TYPE_A = "А";
    private static final String TYPE_T = "Т";
    private static final String TYPE_TP = "Тр";
    private static final String EXPRESS = "Е";

    public static final int MAX_PANEL_SIZE = 24;

    public RunningLineTextCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        Panel panel = handler.getPanel();

        //remove expired text lines
        panel.getRunningTextForRoute()
                .removeIf(s -> s.getExpirationTime().after(new Date()));

        List<Prediction> predictions = handler.getServer().getPredictionService().getPredictions(panel.getStopId());
        LOGGER.info("Number of predictions-{} for the panel-{}", predictions.size(), panel.getLogicalNumber());
        RouteLineRequest.Builder builder = new RouteLineRequest.Builder().addPacketNumber(1).addDate(new Date());

        if (predictions.size() != panel.getViewRouteCounts()) {
            builder.addScheduleLineNumber(0)
                    .addDeviceType("")
                    .addRouteNumber("")
                    .addRouteName("")
                    .addArrivalMinutes("")
                    .addArrivalMinutes("");
        }

        panel.resetViewRouteCounts();
        panel.getRouteResponses().clear();

        StringBuilder panelCurrentText = new StringBuilder();

        for (int i = 0; i < predictions.size() && i < MAX_PANEL_SIZE; i++) {
            Prediction route = predictions.get(i);
            List<Dest> destList = route.getDest();
            Dest dest = destList.get(0);
            List<Pred> predList = dest.getPred();

            String deviceType = route.getRouteShortName().replaceAll("\\d", "");
            deviceType = deviceType.length() > 2 ? deviceType.substring(0, deviceType.length() - 1) : deviceType;

            int indexLast = dest.getHeadsign().indexOf("(");
            String routeName = indexLast != -1 ? dest.getHeadsign().substring(0, indexLast) : dest.getHeadsign();
            String routeNumber = route.getRouteShortName().replaceAll(deviceType, "");
            String bus = deviceType + routeNumber;
            builder.addScheduleLineNumber(i + 1)
                    .addDeviceType(deviceType)
                    .addRouteNumber(routeNumber);

            panelCurrentText.append(routeNumber);
            panelCurrentText.append(" ");
            RouteResponse routeResponse = new RouteResponse();

            //for cases when user wanna post running text line into some route
            if (panel.isRunningTextMode() && !CollectionUtils.isEmpty(panel.getRunningTextForRoute()) && findBus(panel, bus)) {
                addRunningTextForExistentRoute(panel, builder, panelCurrentText, routeResponse, bus, predList);
                continue;
            }

            fillData(panel, builder, panelCurrentText, routeResponse, bus, routeName, predList);
        }

        //adding text for nonexistent route
        addRunningTextForNonexistentRoute(panel, builder, panelCurrentText);

        //when predictions zero other field should be empty
        checkPrediction(predictions, panel);

        String result = builder.build().toString();
        if (!result.endsWith("|</KPT>")) {
            writer.print(result.replace("</KPT>", "|</KPT>"));
        } else {
            writer.print(result);
        }
        panel.setRunningLineText(panelCurrentText.toString());
        panel.setReloadMode(false);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        panel.setTime(simpleDateFormat.format(new Date()));
        LOGGER.info("{}: {}", panel.getLogicalNumber(), result);
    }

    /**
     * Compare buses from prediction and added to panel with text
     *
     * @param panel panel id
     * @param bus   bus number
     * @return result of search
     */
    private boolean findBus(Panel panel, String bus) {
        return panel.getRunningTextForRoute().stream()
                .anyMatch(routes -> routes.getRoute().equals(bus));
    }

    /**
     * Fills needed infos for sending and processing
     */
    private void fillData(Panel panel, RouteLineRequest.Builder builder, StringBuilder panelCurrentText,
                          RouteResponse routeResponse, String bus, String routeName, List<Pred> predList) {
        fillRouteName(bus, routeName, builder, panelCurrentText, routeResponse);

        Pred pred = predList.get(0);
        if (pred == null) {
            return;
        }
        String arrivalMin = pred.getMin() + " хв";
        builder.addArrivalMinutes(arrivalMin);
        builder.addArrivalMinutes("");
        builder.addDateType(pred.getMin() < 1 ? 1 : 0).addLowFloor(false);

        panelCurrentText.append(arrivalMin);
        panelCurrentText.append("|");

        routeResponse.setArrivalMin(arrivalMin);

        panel.incrementViewRouteCounts();
        panel.getRoutes().add(bus);
        panel.getRouteResponses().add(routeResponse);
    }

    /**
     * Fills route name for all needed objects
     */
    private void fillRouteName(String bus, String routeName, RouteLineRequest.Builder builder,
                               StringBuilder panelCurrentText, RouteResponse routeResponse) {
        routeResponse.setBus(bus);
        routeResponse.setRouteName(routeName);

        builder.addRouteName(routeName);

        panelCurrentText.append(routeName);
        panelCurrentText.append(" ");
    }

    /**
     * Finding and adding text to existent route
     */
    private void addRunningTextForExistentRoute(Panel panel, RouteLineRequest.Builder builder, StringBuilder panelCurrentText,
                                                RouteResponse routeResponse, String bus, List<Pred> predList) {
        panel.getRunningTextForRoute().stream()
                .filter(buses -> buses.getRoute().equals(bus))
                .forEach(buses -> {
                    if (buses.isAddToExistingMode() && buses.getText().length() < 60) {
                        fillData(panel, builder, panelCurrentText, routeResponse, bus, buses.getText(), predList);
                    } else {
                        fillBuilderAndPanel(panel, builder, panelCurrentText, routeResponse, bus, buses.getText());
                    }
                });
    }

    /**
     * Finding and adding text to nonexistent route
     */
    private void addRunningTextForNonexistentRoute(Panel panel, RouteLineRequest.Builder builder, StringBuilder panelCurrentText) {
        if (panel.isRunningTextMode() && !CollectionUtils.isEmpty(panel.getRunningTextForRoute())) {
            checkBuses(panel)
                    .forEach(routes -> {
                        RouteResponse routeResponse = new RouteResponse();
                        builder.addScheduleLineNumber(panel.getViewRouteCounts() + 1);
                        splitBus(builder, routes.getRoute());
                        fillBuilderAndPanel(panel, builder, panelCurrentText, routeResponse, routes.getRoute(), routes.getText());
                    });
        }
    }

    /**
     * Fills infos needed for processing extra running text line
     */
    private void fillBuilderAndPanel(Panel panel, RouteLineRequest.Builder builder, StringBuilder panelCurrentText,
                                     RouteResponse routeResponse, String bus, String text) {
        builder.addRouteName(text);

        panelCurrentText.append(text);
        panelCurrentText.append(" ");

        routeResponse.setBus(bus);
        routeResponse.setRouteName(text);

        panel.getRouteResponses().add(routeResponse);
        panel.getRoutes().add(bus);
        panel.incrementViewRouteCounts();
    }

    /**
     * Finds busses which not present in current panel
     *
     * @param panel current panel
     * @return buses
     */
    private List<RunningTextResponse> checkBuses(Panel panel) {
        List<String> buses = panel.getRouteResponses().stream()
                .map(RouteResponse::getBus)
                .collect(Collectors.toList());
        return panel.getRunningTextForRoute().stream()
                .filter(routes -> !buses.contains(routes.getRoute()))
                .collect(Collectors.toList());
    }

    /**
     * Split bus to routeType and routeNumber
     */
    private void splitBus(RouteLineRequest.Builder builder, String bus) {
        String[] routeType;
        if (bus.startsWith(TYPE_A)) {
            routeType = bus.split(TYPE_A);
            builder.addDeviceType(TYPE_A);
        } else if (bus.startsWith(TYPE_T)) {
            routeType = bus.split(TYPE_T);
            builder.addDeviceType(TYPE_T);
        } else if (bus.startsWith(TYPE_TP)) {
            routeType = bus.split(TYPE_TP);
            builder.addDeviceType(TYPE_TP);
        } else if (bus.startsWith(EXPRESS)) {
            builder.addRouteNumber(" ");
            builder.addDeviceType(EXPRESS);
            return;
        } else {
            return;
        }
        builder.addRouteNumber(routeType[1]);
    }

    /**
     * For cases in the end of buses stops work
     * Clean extra line in panel by posting static empty text
     * When work proceed reset static text end continue work
     * in regular way
     */
    private void checkPrediction(List<Prediction> predictions, Panel panel) {
        if (CollectionUtils.isEmpty(predictions) && panel.getViewRouteCounts() == 0 && !panel.isStaticTextMode()) {
            LOGGER.info("Post empty static text for panel-{}", panel.getLogicalNumber());
            writer.print(Constants.POST_EMPTY_STATIC_TEXT);
            panel.setStaticTextMode(true);
            panel.setStaticText("");
        }
        if (!CollectionUtils.isEmpty(predictions) && panel.getViewRouteCounts() > 0
                && panel.isStaticTextMode() && "".equals(panel.getStaticText())) {
            LOGGER.info("Reset empty static text for panel-{}", panel.getLogicalNumber());
            writer.print(Constants.RESET_STATIC_TEXT);
            panel.setStaticTextMode(false);
            panel.setStaticText(null);
        }
    }
}