package com.github.lesach.webapp;

import com.github.lesach.client.log.DLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.github.lesach")
public class StartWebApplication {

    public static void main(String[] args) {
        // Start Application
        SpringApplication.run(StartWebApplication.class, args);
        DLog.info("Application is running...");
    }
}