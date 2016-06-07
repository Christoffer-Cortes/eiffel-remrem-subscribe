package com.ericsson.eiffel.remrem.publish.sse.helper;


import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.List;

@Component("sseHelper") @Slf4j public class SSEHelper {

    private Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();

    public void receive(int port, String routingKey, final List<String> msgs) {
        msgs.clear();

        String uri = "http://localhost:" + port + "/stream?bindingKey=" + routingKey;
        WebTarget target = client.target(uri);
        EventSource eventSource = EventSource.target(target).build();
        EventListener listener = inboundEvent -> {
            String msg = inboundEvent.readData(String.class);
            log.debug("===" + inboundEvent.getName() + "; " + msg);
            msgs.add(msg);
        };
        eventSource.register(listener);
        eventSource.open();
    }

    public void stop() {
        client.close();
    }
}
