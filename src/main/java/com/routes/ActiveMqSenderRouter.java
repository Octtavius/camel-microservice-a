package com.routes;

import com.beans.MessageGenerator;
import com.error.ErrorHandlerConfigurer;
import com.processor.ProcessorGenerateException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Autowired
    private MessageGenerator messageGenerator;

    @Autowired
    private ProcessorGenerateException processorGenerateException;

    @Override
    public void configure() throws Exception {
        new ErrorHandlerConfigurer("my-error-queue")
                .configure(this);

        // every 10 seconds send from timer a message to my-simple-queue
//        from("timer:active-mq-timer-octav?period=4000")
        from("activemq:my-main-queue")
//                .transform().constant("My message for Active MQ")
                .startupOrder(2)
                .bean(messageGenerator)
                .log("${body}")
                .choice()
                .when(body().endsWith("1"))
                .log("*** Ends with 1")
                .to("activemq:my-simple-queue")
                .when(body().endsWith("2"))
                .log("### Ends with 2")
                .to("activemq:second-simple-queue")
                .when(body().endsWith("3"))
                .process(processorGenerateException);// throw an exception here 1
    }
}
