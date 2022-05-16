package com.microgis.server;

import com.microgis.command.PanelTestFixtures;
import com.microgis.controller.dto.panel.Command;
import com.microgis.document.repository.PanelRepository;
import com.microgis.response.PhoneNumberResponse;
import com.microgis.service.PredictionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

    @InjectMocks
    private Server server;

    @Mock
    private ServerSocket serverSocket;

    @Mock
    private PredictionService predictionService;

    @Mock
    private PanelRepository panelRepository;

    @Test(expected = NoSuchElementException.class)
    public void testExecuteCommandException() throws IOException {
        //when
        server.executeCommand(1, Command.ANSWER_SYNC_PACKAGE);
    }

    @Test
    public void testExecuteCommandSuccess() throws IOException {
        //given
        UIEventListener uiEventListener = Mockito.mock(UIEventListener.class);
        when(panelRepository.findAll()).thenReturn(PanelTestFixtures.createPanels());
        Server server = Mockito.spy(new Server(predictionService, serverSocket, panelRepository));
        server.switchModeOnOrOff(4910, "1");
        server.registerCommandListener(4910, uiEventListener);

        //when
        server.executeCommand(4910, Command.ANSWER_SYNC_PACKAGE);

        //then
        verify(uiEventListener, times(1)).execute(Command.ANSWER_SYNC_PACKAGE);
    }

    @Test
    public void testFindListByTypeSuccess() {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380453071937");
        phoneNumberResponse.setPanelId(1);
        server.addResponse(1, phoneNumberResponse);

        //when
        PhoneNumberResponse result = server.findListByType(1, PhoneNumberResponse.class);

        //then
        assertEquals("+380453071937", result.getPhoneNumber());
        assertEquals(1, result.getNumber());
    }

    @Test
    public void testFindListByTypeNull() {
        //when
        PhoneNumberResponse result = server.findListByType(1, PhoneNumberResponse.class);

        //then
        assertNull(result);
    }

    @Test
    public void testResponsesOne() {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380453071937");
        phoneNumberResponse.setPanelId(1);
        server.addResponse(1, phoneNumberResponse);

        //when
        PhoneNumberResponse result = server.getResponse(PhoneNumberResponse.class, 1);

        //then
        assertEquals(1, result.getNumber());
        assertEquals("+380453071937", result.getPhoneNumber());
    }

    @Test
    public void testResponsesMultipleIds() {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380453071937");
        PhoneNumberResponse phoneNumberResponse1 = new PhoneNumberResponse(2, "+380453071938");
        server.addResponse(1, phoneNumberResponse);
        server.addResponse(2, phoneNumberResponse1);

        //when
        PhoneNumberResponse result = server.getResponse(PhoneNumberResponse.class, 1);
        PhoneNumberResponse result1 = server.getResponse(PhoneNumberResponse.class, 2);

        //then
        assertEquals(1, result.getNumber());
        assertEquals(2, result1.getNumber());
        assertEquals("+380453071937", result.getPhoneNumber());
        assertEquals("+380453071938", result1.getPhoneNumber());
    }

    @Test
    public void testResponsesMultiple() {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380453071937");
        PhoneNumberResponse phoneNumberResponse1 = new PhoneNumberResponse(2, "+380453071938");
        server.addResponse(1, phoneNumberResponse);
        server.addResponse(1, phoneNumberResponse1);

        //when
        PhoneNumberResponse result = server.getResponse(PhoneNumberResponse.class, 1);

        //then
        assertEquals(2, result.getNumber());
        assertEquals("+380453071938", result.getPhoneNumber());
    }


    @Test
    public void testResponsesNull() {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380453071937");
        phoneNumberResponse.setPanelId(1);
        server.addResponse(1, phoneNumberResponse);
        server.cleanResponse(PhoneNumberResponse.class, 1);

        //when
        PhoneNumberResponse result = server.getResponse(PhoneNumberResponse.class, 1);

        //then
        assertNull(result);
    }

    @Test
    public void testConvertCustomClassToArray() throws IllegalAccessException {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse(1, "+380453071937");

        //when
        String[] result = server.convertCustomClassToArray(phoneNumberResponse);

        //then
        assertEquals(2, result.length);
        assertEquals("1", result[0]);
        assertEquals("+380453071937", result[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertCustomClassToArrayEmpty() throws IllegalAccessException {
        //given
        PhoneNumberResponse phoneNumberResponse = new PhoneNumberResponse();

        //when
        server.convertCustomClassToArray(phoneNumberResponse);
    }

    @Test
    public void testFindAllPanel() {
        //given
        when(panelRepository.findAll()).thenReturn(PanelTestFixtures.createPanels());

        //when
        server.findAllPanel(panelRepository);

        //then
        assertEquals(6, server.getModeResponses().size());
    }

    @Test
    public void testFindAllPanelEmpty() {
        //given
        when(panelRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        server.findAllPanel(panelRepository);

        //then
        assertTrue(CollectionUtils.isEmpty(server.getModeResponses()));
    }
}