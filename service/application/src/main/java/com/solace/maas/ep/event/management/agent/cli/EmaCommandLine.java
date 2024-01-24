package com.solace.maas.ep.event.management.agent.cli;

import com.solace.maas.ep.event.management.agent.config.eventPortal.EventPortalProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@ConditionalOnProperty(name = "spring.main.web-application-type", havingValue = "none")
@Slf4j
public class EmaCommandLine implements CommandLineRunner {
    private final CommandLineScan commandLineScan;
    private final CommandLineImport commandLineImport;

    private final EventPortalProperties eventPortalProperties;
    private final ConfigurableApplicationContext ctx;
    private final CamelContext camelContext;


    public EmaCommandLine(CommandLineScan commandLineScan,
                          CommandLineImport commandLineImport,
                          EventPortalProperties eventPortalProperties,
                          ConfigurableApplicationContext ctx,
                          CamelContext camelContext) {
        this.commandLineScan = commandLineScan;
        this.commandLineImport = commandLineImport;
        this.eventPortalProperties = eventPortalProperties;
        this.ctx = ctx;
        this.camelContext = camelContext;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            if (args.length >= 1) {

                String type = args[0];

                if ("scan".equals(type)) {
                    if (args.length > 3) {
                        tooManyArguments();
                    } else if (args.length < 3) {
                        notEnoughArguments();
                    } else {
                        String messagingServiceId = args[1];
                        String filePathAndName = args[2];

                        commandLineScan.runScan(messagingServiceId, filePathAndName);
                    }
                } else if ("upload".equals(type)) {
                    if (args.length > 2) {
                        tooManyArguments();
                    } else if (args.length < 2) {
                        notEnoughArguments();
                    } else {
                        String fileNameAndPath = args[1];
                        commandLineImport.runUpload(fileNameAndPath);
                    }
                } else {
                    log.error("Unknown command: {}", type);
                }
            } else {
                notEnoughArguments();
            }
        } finally {
            shutdownSpringBootApplication();
        }
    }

    private void notEnoughArguments() {
        log.error("Not enough arguments passed to the application.");
    }

    private void tooManyArguments() {
        log.error("Too many arguments passed to the application.");
    }

    public void shutdownSpringBootApplication() {
        if (!eventPortalProperties.getGateway().getMessaging().isStandalone()) {
            camelContext.shutdown();
            ctx.close();
        }
    }

}



