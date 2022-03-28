package com.error;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import javax.xml.bind.MarshalException;
import javax.xml.bind.UnmarshalException;

public class ErrorHandlerConfigurer {

    private final String queueName;

//    private final Class[] unrecoverableExceptions = {UnmarshalException.class, MarshalException.class, IllegalArgumentException.class};

    public ErrorHandlerConfigurer(String queueName) {
        this.queueName = queueName;
    }

    public void configure(RouteBuilder routeBuilder) {
//        routeBuilder.onException(unrecoverableExceptions)
//                .log("Error happened. Send to " + queueName)
//                .maximumRedeliveries(0)
//                .useOriginalMessage()
//                .handled(true)
//                .logHandled(true)
//                .to("activemq:" + queueName + ".IMQ");

        routeBuilder.onException(Throwable.class)
                .log("Error happened. Send to " + queueName)
                .maximumRedeliveries(0) // it will try to re-send it.
                .retryAttemptedLogLevel(LoggingLevel.WARN)
//                .useOriginalMessage()
                .asyncDelayedRedelivery()
                .handled(true)
                .process(processFailedEvent())
                .to("activemq:" + queueName + ".DLQ")
                .end();
    }

    // print content of the exchange
    private Processor processFailedEvent() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) {

                System.out.println("$$$$$$$$$$start $$$$$$$$$$");
                System.out.println(exchange.getExchangeId());
                System.out.println(exchange.getIn().getHeaders());
                System.out.println(exchange.getMessage());
                System.out.println(exchange.getIn().getBody());
                System.out.println(exchange.getIn().getMessageId());
                System.out.println();
                System.out.println("$$$$$$$$$$end $$$$$$$$$$");
            }
        };
    }
}
