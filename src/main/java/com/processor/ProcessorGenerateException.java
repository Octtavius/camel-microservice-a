package com.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ProcessorGenerateException implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        throw new Exception("Boom! Something happened");
    }
}
