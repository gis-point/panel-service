package com.microgis.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class GprsResponse extends Response {

    /**
     * This is usually the domain name of the mobile Internet access point,
     * provided by the mobile operator
     */
    @NotNull
    private String address;

    /**
     * Login can be null
     */
    private String login;

    /**
     * Password can be null
     */
    private String password;

    @SuppressWarnings("java:S2637")
    public GprsResponse(Response response) {
        super(response);
        parse();
    }

    private void parse() {
        String[] data = getData();
        this.address = indexInBound(data, 2);
        this.login = indexInBound(data, 3);
        this.password = indexInBound(data, 4);
    }
}
