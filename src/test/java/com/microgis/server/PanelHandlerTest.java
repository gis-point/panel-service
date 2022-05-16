package com.microgis.server;

import com.microgis.command.PanelTestFixtures;
import com.microgis.command.RunningLineTextCommand;
import com.microgis.controller.dto.RouteResponse;
import com.microgis.controller.dto.RoutesModeResponse;
import com.microgis.controller.dto.RunningTextResponse;
import com.microgis.controller.dto.panel.Command;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.document.repository.PanelRepository;
import com.microgis.response.*;
import com.microgis.service.PredictionService;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PanelHandlerTest {

    @InjectMocks
    private PanelHandler panelHandler;

    @Mock
    private PanelWriter writer;

    @Mock
    private PanelReader reader;

    @Mock
    private PredictionService predictionService;

    @Mock
    private ServerSocket serverSocket;

    @Mock
    private PanelRepository panelRepository;

    @Mock
    private Server server;

    @Mock
    private Socket clientSocket;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Test
    @SuppressWarnings("java:S2925")
    void testScheduleRunningLineTaskRuns() throws InterruptedException {
        //given
        RunningLineTextCommand runningLineTextCommand = Mockito.mock(RunningLineTextCommand.class);
        when(server.getModeResponses()).thenReturn(Collections.singletonList(new RoutesModeResponse("", 4997, "0", "", "")));
        Timer timer = new Timer();
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        panel.setTimerMode(true);
        panel.setLogicalNumber(4997);

        //when
        panelHandler.scheduleRunningLineTask(runningLineTextCommand, panelHandler, timer);
        Thread.sleep(20000);
        timer.cancel();
        timer.purge();

        //then
        verify(runningLineTextCommand, times(1)).execute(panelHandler);
    }

    @Test
    @SuppressWarnings("java:S2925")
    void testScheduleRunningLineTaskFailed() throws InterruptedException {
        //given
        RunningLineTextCommand runningLineTextCommand = Mockito.mock(RunningLineTextCommand.class);
        Timer timer = new Timer();
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        panel.setTimerMode(false);
        panel.setLogicalNumber(4997);

        //when
        panelHandler.scheduleRunningLineTask(runningLineTextCommand, panelHandler, timer);
        Thread.sleep(20000);
        timer.cancel();
        timer.purge();

        //then
        verify(runningLineTextCommand, times(0)).execute(panelHandler);
    }

    @Test
    void testAddAndCheckRunningTextLineAdd() {
        //given
        Server server = new Server(predictionService, serverSocket, panelRepository);
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        RunningTextResponse runningTextResponse = new RunningTextResponse("A23", "text", false);
        panel.setRunningTextForRoute(new ArrayList<>(Collections.singletonList(runningTextResponse)));

        //when
        panelHandler.addAndCheckRunningTextLine("A09", "text1", "true");

        //then
        assertEquals(2, panel.getRunningTextForRoute().size());
        assertEquals("A23", panel.getRunningTextForRoute().get(0).getRoute());
        assertEquals("text", panel.getRunningTextForRoute().get(0).getText());
        assertFalse(panel.getRunningTextForRoute().get(0).isAddToExistingMode());
        assertEquals("A09", panel.getRunningTextForRoute().get(1).getRoute());
        assertEquals("text1", panel.getRunningTextForRoute().get(1).getText());
        assertTrue(panel.getRunningTextForRoute().get(1).isAddToExistingMode());
    }

    @Test
    void testAddAndCheckRunningTextLineReplaceLast() {
        //given
        Server server = new Server(predictionService, serverSocket, panelRepository);
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        panel.setRouteResponses(Collections.singletonList(new RouteResponse("A23","test")));
        RunningTextResponse runningTextResponse = new RunningTextResponse("A23", "text", true);
        panel.setRunningTextForRoute(new ArrayList<>(Collections.singletonList(runningTextResponse)));

        //when
        panelHandler.addAndCheckRunningTextLine("A23", "text1", "false");

        //then
        assertEquals(1, panel.getRunningTextForRoute().size());
        assertEquals("A23", panel.getRunningTextForRoute().get(0).getRoute());
        assertEquals("text1|", panel.getRunningTextForRoute().get(0).getText());
        assertFalse(panel.getRunningTextForRoute().get(0).isAddToExistingMode());
    }

    @Test
    void testAddAndCheckRunningTextLineReplace() {
        //given
        Server server = new Server(predictionService, serverSocket, panelRepository);
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        RunningTextResponse runningTextResponse = new RunningTextResponse("A23", "text", true);
        RunningTextResponse runningTextResponse1 = new RunningTextResponse("A24", "text", true);
        panel.setRunningTextForRoute(new ArrayList<>(Arrays.asList(runningTextResponse, runningTextResponse1)));

        //when
        panelHandler.addAndCheckRunningTextLine("A23", "text1", "false");

        //then
        assertEquals(2, panel.getRunningTextForRoute().size());
        assertEquals("A23", panel.getRunningTextForRoute().get(0).getRoute());
        assertEquals("text1", panel.getRunningTextForRoute().get(0).getText());
        assertFalse(panel.getRunningTextForRoute().get(0).isAddToExistingMode());
    }

    @Test
    void testAddAndCheckRunningTextLineNew() {
        //given
        Server server = new Server(predictionService, serverSocket, panelRepository);
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);

        //when
        panelHandler.addAndCheckRunningTextLine("A23", "text1", "false");

        //then
        assertEquals(1, panelHandler.getPanel().getRunningTextForRoute().size());
        assertEquals("A23", panelHandler.getPanel().getRunningTextForRoute().get(0).getRoute());
        assertEquals("text1", panelHandler.getPanel().getRunningTextForRoute().get(0).getText());
        assertFalse(panelHandler.getPanel().getRunningTextForRoute().get(0).isAddToExistingMode());
    }

    @Test
    void testHandleTicketCommand() {
        //given
        Server server = new Server(predictionService, serverSocket, panelRepository);
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        panel.setLogicalNumber(4997);
        Response response = new Response(new StringBuilder("<KPT>1$1022$1$4.02$3$1504$2$24$96$2$865733022082887$2$1</KPT>"));

        //when
        panelHandler.handleTicketCommand(response, panelHandler);

        //then
        assertTrue(panel.isTimerMode());
        assertEquals(1, panelHandler.getServer().getAllPanels().size());
        assertEquals(4997, panelHandler.getServer().getAllPanels().get(0).getLogicalNumber());
    }

    @Test
    void testCheckPanelFlagsStaticText() {
        //given
        List<Panel> panels = new ArrayList<>();
        panels.add(PanelTestFixtures.createPanel());
        panels.get(0).setStaticTextMode(true);
        panels.get(0).setStaticText("test");
        Panel panel = panelHandler.getPanel();
        panel.setLogicalNumber(4997);
        when(server.getAllPanels()).thenReturn(panels);

        //when
        panelHandler.checkPanelFlags(writer, reader, panelHandler);

        //then
        verify(writer, times(2)).print(stringArgumentCaptor.capture());
        List<String> result = stringArgumentCaptor.getAllValues();
        assertEquals("<KPT>16$1$test</KPT>", result.get(0));
        assertEquals("<KPT>303$1</KPT>", result.get(1));
        assertEquals("test", panelHandler.getPanel().getStaticText());
        assertTrue(panelHandler.getPanel().isStaticTextMode());
    }

    @Test
    void testCheckPanelFlagsRunningText() {
        //given
        List<Panel> panels = new ArrayList<>();
        panels.add(PanelTestFixtures.createPanel());
        panels.get(0).setRunningTextMode(true);
        Panel panel = panelHandler.getPanel();
        panel.setLogicalNumber(4997);
        panel.getRunningTextForRoute().add(new RunningTextResponse("A29", "text", false));
        when(server.getAllPanels()).thenReturn(panels);

        //when
        panelHandler.checkPanelFlags(writer, reader, panelHandler);

        //then
        verify(writer, times(1)).print("<KPT>303$1</KPT>");
        assertEquals("A29", panelHandler.getPanel().getRunningTextForRoute().get(0).getRoute());
        assertEquals("text", panelHandler.getPanel().getRunningTextForRoute().get(0).getText());
        assertTrue(panelHandler.getPanel().isRunningTextMode());
    }

    @ParameterizedTest
    @MethodSource("commandProvider")
    void testWriteCommand(List<Response> responses, List<Command> commands) {
        //given
        Server server = new Server(predictionService, serverSocket, panelRepository);
        PanelHandler panelHandler = new PanelHandler(clientSocket, server);
        Panel panel = panelHandler.getPanel();
        panel.setLogicalNumber(4997);

        //when
        IntStream.range(0, 12).forEach(i -> panelHandler.parseReadCommands(responses.get(i), commands.get(i)));

        //then
        PanelDataResponse panelDataResponse = panelHandler.getServer().getResponse(PanelDataResponse.class, 4997);
        assertEquals(59, panelDataResponse.getTextSpeed());
        assertEquals(255, panelDataResponse.getMaxBrightness());
        assertEquals(10, panelDataResponse.getMinBrightness());
        assertTrue(panelDataResponse.isAutomaticMode());
        assertEquals(128, panelDataResponse.getCurrentBrightness());
        assertEquals(0, panelDataResponse.getCurrentIllumination());
        assertFalse(panelDataResponse.isTemperatureMode());
        assertEquals(0, panelDataResponse.getCurrentTemperature(), 0);

        ServerSettingsResponse serverSettingsResponse = panelHandler.getServer()
                .getResponse(ServerSettingsResponse.class, 4997);
        assertEquals("localhost", serverSettingsResponse.getIp());
        assertEquals(5555, serverSettingsResponse.getPort());

        LogicalNumberResponse logicalNumberResponse = panelHandler.getServer()
                .getResponse(LogicalNumberResponse.class, 4997);
        assertEquals(444, logicalNumberResponse.getCurrentNumber());
        assertEquals(333, logicalNumberResponse.getDeterminedNumber());
        assertEquals(555, logicalNumberResponse.getPhysicalNumber());

        GprsResponse gprsResponse = panelHandler.getServer().getResponse(GprsResponse.class, 4997);
        assertEquals("www.test.com", gprsResponse.getAddress());
        assertEquals("login", gprsResponse.getLogin());
        assertEquals("password", gprsResponse.getPassword());

        StopNameResponse stopNameResponse = panelHandler.getServer().getResponse(StopNameResponse.class, 4997);
        assertEquals("Угорська (796)", stopNameResponse.getStopName());

        AdditionalServerSettingsResponse additionalServerSettingsResponse = panelHandler.getServer().
                getResponse(AdditionalServerSettingsResponse.class, 4997);
        assertEquals("localhost", additionalServerSettingsResponse.getIp());
        assertEquals(5555, additionalServerSettingsResponse.getPort());

        TimeZoneResponse timeZoneResponse = panelHandler.getServer().getResponse(TimeZoneResponse.class, 4997);
        assertTrue(timeZoneResponse.isSummerTimeMode());
        assertEquals(2, timeZoneResponse.getTimeZone());

        OperationParametersResponse operationParametersResponse = panelHandler.getServer()
                .getResponse(OperationParametersResponse.class, 4997);
        assertEquals(0, operationParametersResponse.getSortMode().intValue());
        assertEquals(10, operationParametersResponse.getServerTimeout().intValue());
        assertEquals(24, operationParametersResponse.getKeepRouteRecordTime().intValue());
        assertEquals(19, operationParametersResponse.getStartEveningTime().intValue());
        assertEquals(4, operationParametersResponse.getFinishEveningTime().intValue());
        assertEquals(300, operationParametersResponse.getAdditionalServerInterval().intValue());
        assertEquals(2, operationParametersResponse.getAdditionalServerConnectionInterval().intValue());
    }

    @ParameterizedTest
    @MethodSource("packetProvider1")
    void testReadCommandCommand(List<Command> commands, List<String[]> data) {
        //when
        IntStream.range(0, 9).forEach(i -> panelHandler.processWriteCommandsPart3(commands.get(i), writer, reader, 1,
                panelHandler, data.get(i)));

        //then
        verify(writer, times(9)).print(stringArgumentCaptor.capture());
        List<String> result = stringArgumentCaptor.getAllValues();
        assertEquals(9, result.size());
        assertEquals("<KPT>311$1</KPT>", result.get(0));
        assertEquals("<KPT>305$1</KPT>", result.get(1));
        assertEquals("<KPT>304$1$21$94$2$91$1$98$22$3$1$1$1$1$1$2$1$1$2$9$3$3$1$0</KPT>", result.get(2));
        assertEquals("<KPT>306$1$47$1$1$0$2$99$1$5$8$1$15$5$0$30$10$0$8$8$8$12422$120$0$0$0$0$9$0$1$0$0$46$9$0$1$2$3$4</KPT>", result.get(3));
        assertEquals("<KPT>4$1$localhost$login$password$192.168.0.1$8080</KPT>", result.get(4));
        assertEquals("<KPT>15$1$text</KPT>", result.get(5));
        assertEquals("<KPT>14$1$0$Arriving</KPT>", result.get(6));
        assertEquals("<KPT>53$1</KPT>", result.get(7));
        assertEquals("<KPT>51$1</KPT>", result.get(8));
    }

    @ParameterizedTest
    @MethodSource("packetProvider2")
    void testReadCommandCommand1(List<Command> commands, List<String[]> data) {
        //when
        IntStream.range(0, 12).forEach(i -> panelHandler.processWriteCommandsPart2(commands.get(i), writer, reader, 1,
                panelHandler, data.get(i)));

        //then
        verify(writer, times(12)).print(stringArgumentCaptor.capture());
        List<String> result = stringArgumentCaptor.getAllValues();
        assertEquals(12, result.size());
        assertEquals("<KPT>7$1$PHONE1$+380507528134$</KPT>", result.get(0));
        assertEquals("<KPT>50$1$address$login$password</KPT>", result.get(1));
        assertEquals("<KPT>60$1$</KPT>", result.get(2));
        assertEquals("<KPT>60$1$stop name</KPT>", result.get(3));
        assertEquals("<KPT>61$1</KPT>", result.get(4));
        assertEquals("<KPT>307$1</KPT>", result.get(5));
        assertEquals("<KPT>400$1$localhost$1</KPT>", result.get(6));
        assertEquals("<KPT>401$1</KPT>", result.get(7));
        assertEquals("<KPT>402$1$1</KPT>", result.get(8));
        assertEquals("<KPT>321$1</KPT>", result.get(9));
        assertEquals("<KPT>320$1$0$4</KPT>", result.get(10));
        assertEquals("<KPT>310$1$0$0$0$0$0$0$0</KPT>", result.get(11));
    }

    @ParameterizedTest
    @MethodSource("packetProvider3")
    void testReadCommandCommand2(List<Command> commands, List<String[]> data) {
        //when
        IntStream.range(0, 12).forEach(i -> panelHandler.processWriteCommandsPart1(commands.get(i), writer, reader,
                panelHandler, data.get(i)));

        //then
        verify(writer, times(11)).print(stringArgumentCaptor.capture());
        List<String> result = stringArgumentCaptor.getAllValues();
        assertEquals(11, result.size());
        assertEquals("<KPT>5$1</KPT>", result.get(0));
        assertEquals("<KPT>52$1$localhost$1</KPT>", result.get(1));
        assertEquals("<KPT>303$1</KPT>", result.get(2));
        assertEquals("<KPT>16$1$text</KPT>", result.get(3));
        assertEquals("<KPT>17$1</KPT>", result.get(4));
        assertEquals("<KPT>302$1$40$255$100</KPT>", result.get(5));
        assertEquals("<KPT>8$1</KPT>", result.get(6));
        assertEquals("<KPT>20$1</KPT>", result.get(7));
        assertEquals("<KPT>404$1$1111</KPT>", result.get(8));
        assertEquals("<KPT>555$1</KPT>", result.get(9));
        assertEquals("<KPT>558$1</KPT>", result.get(10));
    }

    private static Stream<Arguments> packetProvider1() {
        return Stream.of(arguments(new ArrayList<>(
                        Arrays.asList(
                                Command.READ_OPERATION_PARAMETERS,
                                Command.READ_DISPLAY_STYLE_SCHEDULE_PARAMETERS,
                                Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS,
                                Command.SET_DISPLAY_STYLE_PARAMETERS,
                                Command.SET_COMPLEX_COMMUNICATION_PARAMETERS,
                                Command.READ_TEXT_FOR_TIME_FIELD,
                                Command.SET_TEXT_FOR_TIME_FIELD,
                                Command.GET_SERVER_SETTINGS,
                                Command.READ_GPRS
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                new String[]{},
                                new String[]{},
                                new String[]{"21", "94", "2", "91", "1", "98", "22", "3", "1", "1", "1", "1", "1", "2", "1", "1",
                                        "2", "9", "3", "3", "1", "0"},
                                new String[]{"47", "1", "1", "0", "2", "99", "1", "5", "8", "1", "15", "5", "0", "30", "10", "0",
                                        "8", "8", "8", "12422", "120", "0", "0", "0", "0", "9", "0", "1", "0", "0", "46", "9", "0", "1", "2",
                                        "3", "4"},
                                new String[]{"localhost", "login", "password", "192.168.0.1", "8080"},
                                new String[]{"text"},
                                new String[]{"0", "Arriving"},
                                new String[]{},
                                new String[]{}
                        ))));
    }

    private static Stream<Arguments> packetProvider2() {
        return Stream.of(arguments(new ArrayList<>(
                        Arrays.asList(
                                Command.SET_PHONE_NUMBER,
                                Command.SET_GPRS,
                                Command.SET_STOP_NAME,
                                Command.READ_STOP_NAME,
                                Command.READ_DISPLAY_STYLE_PARAMETERS,
                                Command.SET_ADDITIONAL_SERVER,
                                Command.READ_ADDITIONAL_SERVER,
                                Command.SET_ADDITIONAL_SERVER_TIMEOUT,
                                Command.READ_TIME_ZONE,
                                Command.SET_TIME_ZONE,
                                Command.SET_OPERATION_PARAMETERS,
                                Command.RUNNING_LINE_TEXT
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                new String[]{"1", "+380507528134"},
                                new String[]{"address", "login", "password"},
                                new String[]{"stop name"},
                                new String[]{},
                                new String[]{},
                                new String[]{"localhost", "1"},
                                new String[]{},
                                new String[]{"1"},
                                new String[]{},
                                new String[]{"0", "4"},
                                new String[]{"0", "0", "0", "0", "0", "0", "0"},
                                new String[]{"0", "text", "false"}
                        ))));
    }

    private static Stream<Arguments> packetProvider3() {
        return Stream.of(arguments(new ArrayList<>(
                        Arrays.asList(
                                Command.RELOAD,
                                Command.SERVER_SETTINGS,
                                Command.READ_PHYSICAL_INFO,
                                Command.STATIC_TEXT,
                                Command.RESET_STATIC_TEXT,
                                Command.SET_PHYSICAL_INFO,
                                Command.RESET_ALL_SETTINGS,
                                Command.RESET_TICKER,
                                Command.SYNC_PACKAGE,
                                Command.SET_LOGICAL_NUMBER,
                                Command.RESTART_MODEM,
                                Command.RESETTING_COMMUNICATION_PARAMETERS
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                new String[]{},
                                new String[]{"localhost", "1"},
                                new String[]{},
                                new String[]{"text"},
                                new String[]{},
                                new String[]{"40", "255", "100"},
                                new String[]{},
                                new String[]{"2"},
                                new String[]{},
                                new String[]{"1111"},
                                new String[]{},
                                new String[]{}
                        ))));
    }

    private static Stream<Arguments> commandProvider() {
        return Stream.of(arguments(new ArrayList<>(
                        Arrays.asList(
                                new Response(new StringBuilder("<KPT>302$1$59$255$10$1$128$0$0$0</KPT>")),
                                new Response(new StringBuilder("<KPT>52$1$localhost$5555</KPT>")),
                                new Response(new StringBuilder("<KPT>405$1$444$555$333</KPT>")),
                                new Response(new StringBuilder("<KPT>50$1$www.test.com$login$password</KPT>")),
                                new Response(new StringBuilder("<KPT>21$1</KPT>")),
                                new Response(new StringBuilder("<KPT>60$1$Угорська (796)</KPT>")),
                                new Response(new StringBuilder("<KPT>306$1$47$1$1$0$2$99$1$5$8$1$15$5$0$30$10$0$8$8$8$12422$120$0$0$0$0$9$0$1$0$0$46$9$0$1$2$3$4</KPT>")),
                                new Response(new StringBuilder("<KPT>52$1$localhost$5555</KPT>")),
                                new Response(new StringBuilder("<KPT>320$1$1$2</KPT>")),
                                new Response(new StringBuilder("<KPT>310$1$0$10$24$19$4$300$2</KPT>")),
                                new Response(new StringBuilder("<KPT>304$1$21$94$2$91$1$98$22$3$1$1$1$1$1$2$1$1$2$9$3$3$1$0</KPT>")),
                                new Response(new StringBuilder("<KPT>14$353$0$Прибуває Is Arriving</KPT>"))
                        )),
                new ArrayList<>(
                        Arrays.asList(
                                Command.SET_PHYSICAL_INFO,
                                Command.SERVER_SETTINGS,
                                Command.READ_LOGICAL_NUMBER,
                                Command.SET_GPRS,
                                Command.ANSWER_SYNC_PACKAGE,
                                Command.SET_STOP_NAME,
                                Command.SET_DISPLAY_STYLE_PARAMETERS,
                                Command.SET_ADDITIONAL_SERVER,
                                Command.SET_TIME_ZONE,
                                Command.SET_OPERATION_PARAMETERS,
                                Command.SET_DISPLAY_STYLE_SCHEDULE_PARAMETERS,
                                Command.SET_TEXT_FOR_TIME_FIELD
                        ))));
    }
}