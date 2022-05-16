package com.microgis;

import com.microgis.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PanelApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PanelApplication.class);

    private final Server server;

    public PanelApplication(Server server) {
        this.server = server;
    }

    public static void main(String[] args) {
        SpringApplication.run(PanelApplication.class, args);
    }

    @Override
    public void run(String... args) {
        new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }).start();
    }
}
