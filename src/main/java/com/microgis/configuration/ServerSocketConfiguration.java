package com.microgis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.ServerSocket;

@Configuration
public class ServerSocketConfiguration {

    private AppProperties app;

    @Autowired
    public void setApp(AppProperties app) {
        this.app = app;
    }

    @Bean
    public ServerSocket getSocket() throws IOException {
        return new ServerSocket(app.getServerPort());
    }

}