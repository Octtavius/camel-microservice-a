package com.error;

import org.apache.camel.LoggingLevel;
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
                .useOriginalMessage()
                .asyncDelayedRedelivery()
                .handled(true)
                .to("activemq:" + queueName + ".DLQ")
                .end();
    }
}
