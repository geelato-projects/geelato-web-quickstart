package org.geelato.web.quickstart;

import org.geelato.web.platform.boot.BootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.geelato"})
public class QuickStartApplication extends BootApplication {
    private static Logger logger = LoggerFactory.getLogger(QuickStartApplication.class);

    @Override
    public void run(String... strings) throws Exception {
        logger.info("QuickStartApplication>run");
        super.run(strings);
    }

    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class, args);

    }
}
