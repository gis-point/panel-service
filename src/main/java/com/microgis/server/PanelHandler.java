package com.microgis.server;

import com.microgis.command.*;
import com.microgis.controller.dto.RunningTextResponse;
import com.microgis.controller.dto.panel.Command;
import com.microgis.controller.dto.panel.ErrorType;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.response.*;
import com.microgis.util.Constants;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PanelHandler implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelHandler.class);

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    @Getter
    private final Server server;

    @Getter
    private final Socket clientSocket;

    @Getter
    @Setter
    private Panel panel;

    PanelHandler(Socket socket, Server server) {
        this.panel = new Panel();
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        try (PanelWriter writer = new PanelWriter(clientSocket); PanelReader reader = new PanelReader(clientSocket)) {
            PanelHandler handler = this;
            new AuthenticationCommand(writer, reader).execute(handler);
            RunningLineTextCommand runningLineTextCommand = new RunningLineTextCommand(writer, reader);
            if (panel.isIdentified() && panel.getLogicalNumber() != 0) {
                panel.setIp(clientSocket.getInetAddress().toString());
                panel.setPort(String.valueOf(clientSocket.getLocalPort()));

                //in case of panel reconnection
                checkPanelFlags(writer, reader, handler);

                groupRegisterAndScheduling(writer, reader, handler, timer, runningLineTextCommand);

                //read commands
                Response response;
                while ((response = reader.read()) != null) {
                    Command command = response.getCommand();
                    LOGGER.info("Command into the reader -> {}", command);
                    String result = Arrays.toString(response.getData());
                    LOGGER.debug("Response from the panel {}", result);
                    if (command == Command.TICKET) {
                        handleTicketCommand(response, handler);
                    }
                    if (command == Command.SYNC_PACKAGE) {
                        LOGGER.info("ANSWER_SYNC_PACKAGE for the panel-{}", panel.getLogicalNumber());
                        writer.print(Constants.ANSWER_SYNC_PACKAGE);

                        //restart panel in case when ticket package was not get during 15 sec
                        //and connection with panel still alive and we getting SYNC_PACKAGE
                        LOGGER.info("Restart timer for panel-{}", panel.getLogicalNumber());
                        timer.cancel();
                        timer.purge();
                        timer = new Timer();
                        panel.setTimerMode(true);
                        groupRegisterAndScheduling(writer, reader, handler, timer, runningLineTextCommand);
                    }
                    parseReadCommands(response, command);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred in panel-{} with message-{}", panel.getLogicalNumber(), e.getMessage());
        } finally {
            int id = panel.getLogicalNumber();
            LOGGER.debug("Canceled timer for the panel-{}", id);
            timer.cancel();
            timer.purge();
            server.switchModeOnOrOff(id, "0");
            panel.setRoutes(null);
            panel.setRouteResponses(null);
        }
    }

    /**
     * Group register and timer for reconnection cases
     */
    private void groupRegisterAndScheduling(PanelWriter writer, PanelReader reader, PanelHandler handler, Timer timer,
                                            RunningLineTextCommand runningLineTextCommand) {
        //register and execution write commands
        server.registerCommandListener(panel.getLogicalNumber(),
                (command, data) -> processWriteCommandsPart1(command, writer, reader, handler, data));

        //schedule task
        scheduleRunningLineTask(runningLineTextCommand, handler, timer);
    }

    /**
     * Handle all write command from controllers
     *
     * @param command which want to send
     * @param data    parameters if they needed
     */
    protected void processWriteCommandsPart1(Command command, PanelWriter writer, PanelReader reader, PanelHandler handler,
                                             String... data) {
        int panelId = panel.getLogicalNumber();
        LOGGER.info("Write command -> {} executed for the panelId-{}", command, panelId);
        if (command == Command.RELOAD) {
            writer.print(Constants.RELOAD);
        } else if (command == Command.SERVER_SETTINGS) {
            new ServerSettingsCommand(writer, reader).execute(handler, data);
            server.cleanResponse(ServerSettingsResponse.class, panelId);
        } else if (command == Command.READ_PHYSICAL_INFO) {
            writer.print(Constants.READ_PHYSICAL_INFO);
        } else if (command == Command.STATIC_TEXT) {
            new StaticTextCommand(writer, reader).execute(handler, data);
            panel.setStaticTextMode(true);
            panel.setStaticText(data[0]);
        } else if (command == Command.RESET_STATIC_TEXT) {
            panel.setStaticTextMode(false);
            panel.setStaticText(null);
            writer.print(Constants.RESET_STATIC_TEXT);
        } else if (command == Command.SET_PHYSICAL_INFO) {
            new AdjustPhysicalInfoCommand(writer, reader).execute(handler, data);
            server.cleanResponse(PanelDataResponse.class, panelId);
        } else if (command == Command.RESET_ALL_SETTINGS) {
            writer.print(Constants.RESET_ALL_SETTINGS);
        } else if (command == Command.RESET_TICKER) {
            panel.getRunningTextForRoute()
                    .removeIf(routes -> routes.getRoute().equals(data[0]));
            if (CollectionUtils.isEmpty(panel.getRunningTextForRoute())) {
                panel.setRunningTextMode(false);
            }
        } else if (command == Command.SYNC_PACKAGE) {
            server.cleanResponse(Response.class, panelId);
            writer.print(Constants.SYNC_PACKAGE);
        } else if (command == Command.SET_LOGICAL_NUMBER) {
            new LogicalNumberCommand(writer, reader).execute(handler, data);
            server.cleanResponse(LogicalNumberResponse.class, panelId);
        } else if (command == Command.RESTART_MODEM) {
            writer.print(Constants.RESTART_MODEM);
        } else if (command == Command.RESETTING_COMMUNICATION_PARAMETERS) {
            writer.print(Constants.RESETTING_COMMUNICATION_PARAMETERS);
        }
        processWriteCommandsPart2(command, writer, reader, panelId, handler, data);
    }

    /**
     * Handle all write command from controllers
     *
     * @param command which want to send
     * @param data    parameters if they needed
     */
    protected void processWriteCommandsPart2(Command command, PanelWriter writer, PanelReader reader, int panelId,
                                             PanelHandler handler, String... data) {
        if (command == Command.SET_PHONE_NUMBER) {
            new PhoneNumberCommand(writer, reader).execute(handler, data);
        } else if (command == Command.SET_GPRS) {
            new GprsCommand(writer, reader).execute(handler, data);
            server.cleanResponse(GprsResponse.class, panelId);
        } else if (command == Command.SET_STOP_NAME) {
            writer.print(Constants.CLEAN_STOP_NAME);
            new StopNameCommand(writer, reader).execute(handler, data);
        } else if (command == Command.RUNNING_LINE_TEXT) {
            addAndCheckRunningTextLine(data);
        } else if (command == Command.READ_STOP_NAME) {
            writer.print(Constants.READ_STOP_NAME);
            server.cleanResponse(StopNameResponse.class, panelId);
        } else if (command == Command.READ_DISPLAY_STYLE_PARAMETERS) {
            writer.print(Constants.READ_DISPLAY_STYLE_PARAMETERS);
        } else if (command == Command.SET_ADDITIONAL_SERVER) {
            new AdditionalServerSettingsCommand(writer, reader).execute(handler, data);
            server.cleanResponse(AdditionalServerSettingsResponse.class, panelId);
        } else if (command == Command.READ_ADDITIONAL_SERVER) {
            writer.print(Constants.READ_ADDITIONAL_SERVER);
        } else if (command == Command.SET_ADDITIONAL_SERVER_TIMEOUT) {
            new AdditionalServerSettingsTimeoutCommand(writer, reader).execute(handler, data);
        } else if (command == Command.READ_TIME_ZONE) {
            writer.print(Constants.READ_TIME_ZONE);
        } else if (command == Command.SET_TIME_ZONE) {
            new TimeZoneCommand(writer, reader).execute(handler, data);
            server.cleanResponse(TimeZoneResponse.class, panelId);
        } else if (command == Command.SET_OPERATION_PARAMETERS) {
            new OperationParametersCommand(writer, reader).execute(handler, data);
            server.cleanResponse(OperationParametersResponse.class, panelId);
        }
        processWriteCommandsPart3(command, writer, reader, panelId, handler, data);
    }

    /**
     * Handle all write command from controllers
     *
     * @param command which want to send
     * @param data    parameters if they needed
     */
    protected void processWriteCommandsPart3(Command command, PanelWriter writer, PanelReader reader, int panelId,
                                             PanelHandler handler, String... data) {
        if (command == Command.READ_OPERATION_PARAMETERS) {
            writer.print(Constants.READ_OPERATION_PARAMETERS);
        } else if (command == Command.READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS) {
            writer.print(Constants.READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS);
        } else if (command == Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS) {
            new DisplayStyleScheduleParameterCommand(writer, reader).execute(handler, data);
            server.cleanResponse(DisplayStyleScheduleParametersResponse.class, panelId);
        } else if (command == Command.SET_DISPLAY_STYLE_PARAMETERS) {
            new DisplayStyleParametersCommand(writer, reader).execute(handler, data);
            server.cleanResponse(DisplayStyleParametersResponse.class, panelId);
        } else if (command == Command.SET_COMPLEX_COMMUNICATION_PARAMETERS) {
            new ComplexCommunicationParametersCommand(writer, reader).execute(handler, data);
        } else if (command == Command.READ_TEXT_FOR_TIME_FIELD) {
            server.cleanResponse(TimeFieldTextResponse.class, panelId);
            writer.print(Constants.READ_TEXT_FOR_TIME_FIELD + data[0] + "</KPT>");
        } else if (command == Command.SET_TEXT_FOR_TIME_FIELD) {
            new TimeFieldTextCommand(writer, reader).execute(handler, data);
            server.cleanResponse(TimeFieldTextResponse.class, panelId);
        } else if (command == Command.GET_SERVER_SETTINGS) {
            writer.print(Constants.SERVER_SETTINGS);
        } else if (command == Command.READ_GPRS) {
            writer.print(Constants.READ_GPRS);
        }
    }

    /**
     * Run runningLineTextCommand every 15 sec
     */
    protected void scheduleRunningLineTask(RunningLineTextCommand runningLineTextCommand, PanelHandler handler, Timer timer) {
        AtomicInteger failCounter = new AtomicInteger(0);
        server.getModeResponses()
                .stream()
                .filter(value -> value.getNumber() == panel.getLogicalNumber() && value.getNumber() != 0)
                .findFirst()
                .ifPresent(start -> {
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (panel.isTimerMode()) {
                                runningLineTextCommand.execute(handler);
                                failCounter.set(0);
                            } else {
                                //when we stop getting TICKET command from panel, we stop timer
                                terminateTimerAndSocket(timer, failCounter.getAndIncrement());
                            }
                            if (failCounter.get() == 0) {
                                panel.setTimerMode(false);
                            }
                        }
                    };
                    timer.scheduleAtFixedRate(timerTask, 15000, 15000);
                    LOGGER.debug("Created timer for panel-{}", panel.getLogicalNumber());
                });
    }

    /**
     * Cancel timer and close socket connection
     */
    private void terminateTimerAndSocket(Timer timer, int failCounter) {
        LOGGER.info("Didn't get TICKET for panel - {} number of fail - {}", panel.getLogicalNumber(), failCounter);
        if (failCounter == 3) {
            LOGGER.info("Lost connection with panel-{}", panel.getLogicalNumber());
            timer.cancel();
            timer.purge();
            panel.setRoutes(new HashSet<>());
            panel.setRouteResponses(new ArrayList<>());
            server.switchModeOnOrOff(panel.getLogicalNumber(), "0");
        }
    }

    /**
     * Adds text to running text line
     * if route already exist just rewrite info
     */
    protected void addAndCheckRunningTextLine(String... data) {
        panel.setRunningTextMode(true);
        List<RunningTextResponse> runningTextResponses = panel.getRunningTextForRoute();
        String routeName = formatRouteNameForLastBus(data);
        Date expirationTime = getExpirationDate(data);
        if (!CollectionUtils.isEmpty(runningTextResponses)) {
            Optional<RunningTextResponse> runningTextResponse = runningTextResponses.stream()
                    .filter(routes -> routes.getRoute().equals(data[0]))
                    .findFirst();
            if (runningTextResponse.isPresent()) {
                runningTextResponse.get().setText(routeName);
                runningTextResponse.get().setAddToExistingMode(Boolean.parseBoolean(data[2]));
            } else {
                panel.getRunningTextForRoute().add(new RunningTextResponse(data[0], routeName, Boolean.parseBoolean(data[2]), expirationTime));
            }
            return;
        }
        panel.getRunningTextForRoute().add(new RunningTextResponse(data[0], routeName, Boolean.parseBoolean(data[2]), expirationTime));
    }

    private Date getExpirationDate(String[] data) {
        Date expirationTime = null;
        if (data.length > 3 && data[3] != null) {
            try {
                expirationTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(data[3]);
            } catch (ParseException e) {
                LOGGER.error("Couldn't parse date - {}", e.getMessage());
            }
        }
        return expirationTime;
    }

    private String formatRouteNameForLastBus(String[] data) {
        String routeName = data[1];
        if (!CollectionUtils.isEmpty(panel.getRouteResponses()) &&
                panel.getRouteResponses().get(panel.getRouteResponses().size() - 1).getBus().equals(data[0])) {
            routeName = data[1] + "|";
        }
        return routeName;
    }

    /**
     * Handle TICKET package from the panel
     *
     * @param response from panel
     */
    protected void handleTicketCommand(Response response, PanelHandler handler) {
        TicketResponse ticketResponse = new TicketResponse(response);
        //handle error codes if they exist
        if (ticketResponse.getErrorType() != ErrorType.NONE) {
            String errorText = ticketResponse.getErrorType().getText();
            int id = panel.getLogicalNumber();
            LOGGER.error("Error from panel - {} with text - {}", id, errorText);
        }
        int id = panel.getLogicalNumber();
        panel.setTimerMode(true);
        setDateAndMode(id);
        server.addPanelHandler(id, handler);
    }

    private void setDateAndMode(int panelId) {
        server.switchModeOnOrOff(panelId, "1");
        server.getModeResponses().stream()
                .filter(value -> value.getNumber() == panelId)
                .findFirst()
                .ifPresent(value -> value.setLastConnection(simpleDateFormat.format(new Date())));
    }

    /**
     * Parse all read commands
     * response from panel
     *
     * @param response from panel
     * @param command  which need to be parsed
     */
    protected void parseReadCommands(Response response, Command command) {
        int panelId = panel.getLogicalNumber();
        Response responseToSend = null;
        if (command == Command.SET_PHYSICAL_INFO) {
            responseToSend = new PanelDataResponse(response);
            panel.setPanelDataResponse((PanelDataResponse) responseToSend);
        } else if (command == Command.SERVER_SETTINGS) {
            responseToSend = new ServerSettingsResponse(response);
        } else if (command == Command.READ_LOGICAL_NUMBER) {
            responseToSend = new LogicalNumberResponse(response);
        } else if (command == Command.SET_GPRS) {
            responseToSend = new GprsResponse(response);
        } else if (command == Command.ANSWER_SYNC_PACKAGE) {
            responseToSend = new AnswerSyncResponse(panelId);
        } else if (command == Command.SET_STOP_NAME) {
            responseToSend = new StopNameResponse(response);
        } else if (command == Command.SET_DISPLAY_STYLE_PARAMETERS) {
            responseToSend = new DisplayStyleParametersResponse(response);
        } else if (command == Command.SET_ADDITIONAL_SERVER) {
            responseToSend = new AdditionalServerSettingsResponse(response);
        } else if (command == Command.SET_TIME_ZONE) {
            responseToSend = new TimeZoneResponse(response);
        } else if (command == Command.SET_OPERATION_PARAMETERS) {
            responseToSend = new OperationParametersResponse(response);
        } else if (command == Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS) {
            responseToSend = new DisplayStyleScheduleParametersResponse(response);
        } else if (command == Command.SET_TEXT_FOR_TIME_FIELD) {
            responseToSend = new TimeFieldTextResponse(response);
        }
        if (responseToSend != null) {
            responseToSend.setPanelId(panelId);
            server.addResponse(panelId, responseToSend);
        }
    }

    /**
     * In the case of panel reconnection, we copy main settings to prevent data from losing
     */
    protected void checkPanelFlags(PanelWriter writer, PanelReader reader, PanelHandler handler) {
        server.getAllPanels().stream()
                .filter(storedPanel -> storedPanel.getLogicalNumber() == panel.getLogicalNumber())
                .forEach(storedPanel -> {
                    if (storedPanel.isStaticTextMode()) {
                        String text = storedPanel.getStaticText();
                        new StaticTextCommand(writer, reader).execute(handler, text);
                        panel.setStaticTextMode(true);
                        panel.setStaticText(text);
                    }
                    if (storedPanel.isRunningTextMode()) {
                        panel.setRunningTextMode(true);
                        panel.getRunningTextForRoute().addAll(storedPanel.getRunningTextForRoute());
                    }
                    panel.setViewRouteCounts(storedPanel.getViewRouteCounts());
                    panel.setPanelDataResponse(storedPanel.getPanelDataResponse());
                    panel.setTimerMode(true);
                });
        if (panel.getPanelDataResponse() == null) {
            writer.print(Constants.READ_PHYSICAL_INFO);
        }
    }
}