package com.microgis.response;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.microgis.controller.dto.panel.Command;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

    private StringBuilder builder;

    @Before
    public void setUp() {
        builder = new StringBuilder("<KPT>1$1022$1$4.02$3$1504$0$24$96$0$865733022082887$2$1</KPT>");
    }

    @Test
    public void create() {
        Response ticket = new Response(builder);
        assertEquals(Command.TICKET, ticket.getCommand());
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(Response.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.STRICT_INHERITANCE)
                .verify();
    }

    @Test
    public void testToString() {
        ToStringVerifier.forClass(Response.class).verify();
    }
}