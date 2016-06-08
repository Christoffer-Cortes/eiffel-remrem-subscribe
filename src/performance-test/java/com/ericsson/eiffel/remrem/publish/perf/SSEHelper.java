package com.ericsson.eiffel.remrem.publish.perf;


import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.sse.EventListener;
import org.glassfish.jersey.media.sse.EventSource;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.stereotype.Component;

import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Slf4j public class SSEHelper {

    public void receive(Client client, String host, int port, String routingKey, final List<String> msgs) {
        msgs.clear();

        //String uri = "http://" + host + ":" + port + "/stream?bindingKey=" + routingKey;
        String uri = "http://" + host + ":" + port + "/subscribe/stream?bindingKey=" + routingKey;
        WebTarget target = client.target(uri);
        EventSource eventSource = EventSource.target(target).build();
        EventListener listener = inboundEvent -> {
            String msg = inboundEvent.readData(String.class);
            //log.debug("===" + inboundEvent.getName() + "; " + msg);
            msgs.add(msg);
        };
        eventSource.register(listener);
        eventSource.open();
    }

}
