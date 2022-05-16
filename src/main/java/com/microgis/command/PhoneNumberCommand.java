package com.microgis.command;

import com.microgis.request.PhoneNumberRequest;
import com.microgis.response.Response;
import com.microgis.server.PanelHandler;
import com.microgis.util.PanelReader;
import com.microgis.util.PanelWriter;

public class PhoneNumberCommand extends Command {

    public PhoneNumberCommand(PanelWriter writer, PanelReader reader) {
        super(writer, reader);
    }

    @Override
    public void execute(PanelHandler handler, String... data) {
        PhoneNumberRequest phoneNumberRequest;
        //remove phone number
        if (Response.indexInBound(data, 1) == null) {
            phoneNumberRequest = new PhoneNumberRequest.Builder()
                    .addPacketNumber(1)
                    .addNumber(Integer.parseInt(data[0]))
                    .addPhoneNumber("")
                    .build();
        } else {
            //set phone number
            phoneNumberRequest = new PhoneNumberRequest.Builder()
                    .addPacketNumber(1)
                    .addNumber(Integer.parseInt(data[0]))
                    .addPhoneNumber(data[1])
                    .build();
        }
        String request = phoneNumberRequest.toString();
        writer.print(request);
    }
}