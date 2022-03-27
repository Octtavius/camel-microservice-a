package com.error.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import sun.net.www.MessageHeader;

public class MyErrorRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("activemq:my-error-queue.DLQ")
                .routeId(MyErrorRoute.class.getSimpleName())
                .startupOrder(2)
                .log("Received an error event.");
//                .process()
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
                exchange.getIn().setHeader("RedeliveryCount", retryCounter);

                exchange.setProperty("DELAY", 2000 * retryCounter);
            }
        };
    }
}
