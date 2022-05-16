package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StopNameResponseTest {

    @Test
    public void testParse() {
        //when
        Response response = new Response(new StringBuilder("<KPT>60$1$Угорська (796)</KPT>"));

        //then
        assertNotNull(response.getData());
        assertEquals(Command.SET_STOP_NAME, response.getCommand());
        StopNameResponse stopNameResponse = new StopNameResponse(response);
        assertEquals("Угорська (796)", stopNameResponse.getStopName());
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(StopNameResponse.class)
                .verify();
    }

    @Test
    public void testToString() {
        ToStringVerifier.forClass(StopNameResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}