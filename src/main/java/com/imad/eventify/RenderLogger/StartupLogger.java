package com.imad.eventify.RenderLogger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class StartupLogger {
        @Value("${spring.datasource.url:NOT_SET}")
        private String dbUrl;

        @Value("${spring.datasource.username:NOT_SET}")
        private String dbUser;

        @PostConstruct
        public void log() {
            System.out.println("👉 DB URL: " + dbUrl);
            System.out.println("👉 DB USER: " + dbUser);
        }
}
