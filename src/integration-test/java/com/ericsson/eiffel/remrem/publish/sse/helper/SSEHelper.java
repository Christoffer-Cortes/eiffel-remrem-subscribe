package com.ericsson.eiffel.remrem.publish.sse.helper;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@Component("sseHelper") @Slf4j public class SSEHelper {

    @Getter private final List<String> msgs = new ArrayList<>();

    public void receive(int port, String routingKey, long time)
        throws IOException, InterruptedException, TimeoutException {
        new Worker(port, routingKey, time).start();
    }

    private class Worker extends Thread {

        public Worker(int port, String routingKey, long time) {
            this.port = port;
            this.routingKey = routingKey;
            this.time = time;
        }

        int port;
        String routingKey;
        long time;

        @Override public void run() {
            msgs.clear();

            long begin = System.currentTimeMillis();
            //localhost:8080/stream?bindingKey=333
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            String uri = "http://localhost:" + port + "/stream?bindingKey=" + routingKey;
            WebTarget target =
                client.target(uri);

            EventInput eventInput = target.request().get(EventInput.class);
            while (!eventInput.isClosed()) {
                final InboundEvent inboundEvent = eventInput.read();
                long now = System.currentTimeMillis();
                if (now - begin > time) {
                    System.out.println("===timeout: break");
                    break;
                }
                if (inboundEvent == null) {
                    System.out.println("===continue");
                    continue;
                }
                String msg = inboundEvent.readData(String.class);
                System.out
                    .println("============"+inboundEvent.getName() + "; " + msg);

                msgs.add(msg);
            }
        }

    }
}
