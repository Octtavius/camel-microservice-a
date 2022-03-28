package com.routes;

import com.beans.MessageGenerator;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageGeneratorRoute extends RouteBuilder {

    @Autowired
    private MessageGenerator messageGenerator;

    @Override
    public void configure() throws Exception {
        from("timer:active-mq-timer-octav?period=4000")
                .startupOrder(1)
                .bean(messageGenerator)
                .to("activemq:my-main-queue");
    }
}
