package com.error.route;

import com.Headers;
import com.processor.ProcessorGenerateException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyErrorRoute extends RouteBuilder {
    @Autowired
    private ProcessorGenerateException processorGenerateException;

    @Override
    public void configure() throws Exception {
        from("activemq:my-error-queue.DLQ")
                .routeId(MyErrorRoute.class.getSimpleName())
                .startupOrder(3)
                .log("Received an error event.")
                .log("^^ ${body}")
                .process(processFailedEvent())
                .choice()
                .when(header(Headers.RedeliveryCount.name()).isGreaterThan(1))
                    .log("Exhausted retries, sending to IMQ. ")
                    .to("activemq:my-error-queue.IMQ")
                .stop()
                .endChoice()
                .otherwise()
                    .log("Attempt " + Headers.RedeliveryCount + ": Retrying event. ")
                    .log("delaying the retry by " + exchangeProperty("DELAY"))
                .log("Resending the message ${headers.JMSMessageID} to the main queue ")
                    .delay(exchangeProperty("DELAY"))
                    .asyncDelayed()
                .to("activemq:my-main-queue")
                .stop()
                .endChoice()
                .end()
                .log("end error route")
                .stop();

    }

    private Processor processFailedEvent() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) {

                int retryCounter = 0;
                if (exchange.getIn().getHeaders().containsKey("RedeliveryCount")) {
                    retryCounter = (int) exchange.getIn().getHeader("RedeliveryCount");
                }
                retryCounter = retryCounter + 1;
                log.info("Retrying an event that threw exception " + exchange.getExchangeId() + " with the message: \n" + exchange.getIn().getBody());
                log.info("RedeliveryCount is " + retryCounter);

                exchange.getIn().setHeader("RedeliveryCount", retryCounter);

                exchange.setProperty("DELAY", 2000 * retryCounter);
            }
        };
    }
}
