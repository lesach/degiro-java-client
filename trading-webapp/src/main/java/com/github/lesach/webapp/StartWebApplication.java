package com.github.lesach.webapp;

import com.github.lesach.client.log.DLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = { "com.github.lesach" })
public class StartWebApplication {

    public static void main(String[] args) {
        // Start Application
        try {
            SpringApplication.run(StartWebApplication.class, args);
            DLog.info("Application is running...");
        }
        catch (Exception e) {
            DLog.error("ERROR during execution", e);
        }
    }

}