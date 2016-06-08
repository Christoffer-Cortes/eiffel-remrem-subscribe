package com.ericsson.eiffel.remrem.publish.perf;

import org.glassfish.jersey.media.sse.SseFeature;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class PerfTest {

    private SSEHelper sseHelper = new SSEHelper();
    // test duration in seconds
    private static final long DURATION = 60;
    // msg rate per sec
    private static final long RATE = 10;

    @Test(threadPoolSize = 500, invocationCount = 500) public void testMethod()
        throws InterruptedException {
        final List<String> msgs = new ArrayList<>();
        Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();

        sseHelper.receive(client, "142.133.130.137", 8080, "333", msgs);

        //Thread.sleep(TimeUnit.SECONDS.toMillis(DURATION));
        Thread.sleep(TimeUnit.SECONDS.toMillis(DURATION + 1));

        Long id = Thread.currentThread().getId();
        System.out.println("Test method executing on thread with id: " + id + " msgs.size(): " + msgs.size());

        long expectedMsgNumber = RATE * DURATION;
        Assert.assertTrue(msgs.size() >= expectedMsgNumber);

        client.close();
    }
}
