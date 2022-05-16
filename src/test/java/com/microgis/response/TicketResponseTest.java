package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicketResponseTest {

    private StringBuilder builder;

    @Before
    public void setUp() {
        builder = new StringBuilder("<KPT>1$1022$1$4.02$3$1504$0$24$96$0$865733022082887$2$1</KPT>");
    }

    @Test
    public void parse() {
        TicketResponse ticket = new TicketResponse(builder);
        assertEquals(Command.TICKET, ticket.getCommand());
        assertEquals(1, ticket.getLogicalNumber());
        assertEquals(1504, ticket.getBufferSize());
        assertEquals(3, ticket.getScheduleLineCount());
        assertTrue(4.02 - ticket.getPanelVersion() <= 0);
        assertEquals(96, ticket.getMaxEndPointNameLength());
        assertEquals(0, ticket.getViewSate());
        assertEquals(24, ticket.getMaxRoutCount());
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSubclass(TicketResponse.class)
                .verify();
    }

    @Test
    public void testToString() {
        ToStringVerifier.forClass(TicketResponse.class)
                .withIgnoredFields("panelId", "data", "command")
                .verify();
    }
}