package com.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class SimpleLoggingProcessorComponent implements Processor {

    private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessorComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLoggingProcessorComponent {}", exchange.getMessage().getBody());
    }
}
