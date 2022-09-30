package com.solace.maas.ep.runtime.agent.processor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
@Data
@NoArgsConstructor
public class LoggingProcessor implements Processor {
    private String loggerName;

    public LoggingProcessor(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("data collection completed");
    }
}
