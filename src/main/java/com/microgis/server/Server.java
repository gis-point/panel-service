package com.microgis.server;

import com.microgis.controller.dto.RoutesModeResponse;
import com.microgis.controller.dto.panel.Command;
import com.microgis.controller.dto.panel.Panel;
import com.microgis.document.Panels;
import com.microgis.document.repository.PanelRepository;
import com.microgis.response.Response;
import com.microgis.service.PredictionService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final ServerSocket serverSocket;

    private final PredictionService predictionService;

    private final Map<Integer, UIEventListener> listeners = new ConcurrentHashMap<>();

    private final Map<Integer, PanelHandler> handlers = new ConcurrentHashMap<>();

    private final Map<Integer, Set<Response>> responses = new ConcurrentHashMap<>();

    @Getter
    private final List<RoutesModeResponse> modeResponses = new CopyOnWriteArrayList<>();

    public Server(PredictionService predictionService, ServerSocket serverSocket, PanelRepository panelRepository) {
        this.predictionService = predictionService;
        this.serverSocket = serverSocket;
        findAllPanel(panelRepository);
    }

    /**
     * Fetching all panels from db
     */
    protected void findAllPanel(PanelRepository panelRepository) {
        List<Panels> panels = panelRepository.findAll();
        if (CollectionUtils.isEmpty(panels)) {
            LOGGER.info("Panels db empty check mongo instance");
            return;
        }
        panels.forEach(panel -> {
            RoutesModeResponse routesModeResponse = new RoutesModeResponse();
            routesModeResponse.setStopName(panel.getStopName());
            routesModeResponse.setPhoneNumber(panel.getPhoneNumber());
            routesModeResponse.setNumber(panel.getNumber());
            modeResponses.add(routesModeResponse);
        });
    }

    @SuppressWarnings({"java:S2189", "InfiniteLoopStatement"})
    public void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            PanelHandler panelHandler = new PanelHandler(socket, this);
            new Thread(panelHandler).start();
        }
    }

    public PredictionService getPredictionService() {
        return predictionService;
    }

    public void registerCommandListener(int panelId, UIEventListener eventListener) {
        this.listeners.put(panelId, eventListener);
    }

    public void addPanelHandler(int panelId, PanelHandler panelHandler) {
        handlers.put(panelId, panelHandler);
    }

    /**
     * Returns all panels which were register into system
     */
    public List<Panel> getAllPanels() {
        return handlers.values()
                .stream()
                .map(PanelHandler::getPanel)
                .collect(Collectors.toList());
    }

    public void executeCommand(int panelId, Command command, String... data) throws IOException {
        UIEventListener uiEventListener = this.listeners.get(panelId);
        boolean powerMode = checkPanelPower(panelId);
        if (uiEventListener != null && powerMode) {
            uiEventListener.execute(command, data);
        } else {
            throw new NoSuchElementException("There are not such panel into server");
        }
    }

    /**
     * For posting any write command,
     * the panel also should works not only be present in the system.
     */
    private boolean checkPanelPower(int panelId) {
        return modeResponses.stream()
                .filter(panel -> panel.getNumber() == panelId)
                .anyMatch(panel -> "1".equals(panel.getMode()));
    }

    /**
     * Checks and wait response from the panel
     *
     * @param id   panel id
     * @param type type which we expected to find
     * @param <T>  type of response, should be extended from {@link Response}
     * @return response from the panel
     */
    public <T extends Response> T findListByType(int id, Class<T> type) {
        T response = null;
        long start = System.currentTimeMillis();
        long finish = 0L;
        while ((finish - start) < 5000) {
            response = getResponse(type, id);
            if (response != null) {
                break;
            }
            finish = System.currentTimeMillis();
        }
        return response;
    }

    /**
     * Adding any type extended from {@link Response}
     *
     * @param panelId  panel id
     * @param response any type, should be extended from {@link Response}
     * @param <T>      type of response, should be extended from {@link Response}
     */
    public <T extends Response> void addResponse(int panelId, T response) {
        Integer key = responses.keySet().stream()
                .filter(id -> id == panelId)
                .findFirst()
                .orElse(null);
        if (key != null) {
            findResponseList(panelId).ifPresent(values -> values.add(response));
        } else {
            Set<Response> responseList = new HashSet<>();
            responseList.add(response);
            responses.put(panelId, responseList);
        }
    }

    /**
     * Clean any type which was set
     *
     * @param type    class type, should be extended from {@link Response}
     * @param panelId panel id
     * @param <T>     type of response, should be extended from {@link Response}
     */
    public <T extends Response> void cleanResponse(Class<T> type, int panelId) {
        findResponseList(panelId).ifPresent(values -> values.removeIf(type::isInstance));
    }

    /**
     * Finds list with responses from the panels by id
     *
     * @param panelId panel number
     * @return list of responses
     */
    private Optional<Set<Response>> findResponseList(int panelId) {
        return responses.entrySet().stream()
                .filter(entry -> entry.getKey() == panelId)
                .findFirst()
                .map(Map.Entry::getValue);
    }

    /**
     * Created to returns object by type
     *
     * @param type    class type, should be extended from {@link Response}
     * @param panelId panel id
     * @param <T>     type of response, should be extended from {@link Response}
     * @return any type which extended from {@link Response}
     */
    @SuppressWarnings("unchecked")
    public <T extends Response> T getResponse(Class<T> type, int panelId) {
        return (T) responses.entrySet().stream()
                .filter(entry -> entry.getKey() == panelId)
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .filter(type::isInstance)
                .findFirst()
                .orElse(null);
    }

    /**
     * Convert values from class object fields into string array
     * Created to reduce numbers of getters in controller
     */
    @SuppressWarnings("java:S3011")
    public <T> String[] convertCustomClassToArray(T type) throws IllegalAccessException {
        Field[] fields = type.getClass().getDeclaredFields();
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String value = String.valueOf(fields[i].get(type));
            if ("null".equals(value)) {
                throw new IllegalArgumentException();
            }
            values[i] = value;
        }
        return values;
    }

    /**
     * For checking panel status
     *
     * @param panelId panel id
     * @param mode    panel status, should be 0(off) or 1(on)
     */
    public void switchModeOnOrOff(int panelId, String mode) {
        modeResponses.stream()
                .filter(route -> route.getNumber() == panelId)
                .forEach(route -> route.setMode(mode));
    }
}