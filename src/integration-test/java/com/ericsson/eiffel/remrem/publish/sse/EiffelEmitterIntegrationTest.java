package com.ericsson.eiffel.remrem.publish.sse;

import com.ericsson.eiffel.remrem.publish.sse.helper.RMQHelper;
import com.ericsson.eiffel.remrem.publish.sse.helper.SSEHelper;
import com.ericsson.eiffel.remrem.subscribe.App;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class) @SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class EiffelEmitterIntegrationTest {

    @Value("${local.server.port}") private int port;

    @Autowired @Qualifier("rmqHelper") RMQHelper rmqHelper;
    @Autowired @Qualifier("sseHelper") SSEHelper sseHelper;

    @Before public void setUp() {
        Assert.assertNotNull(rmqHelper);
        Assert.assertNotNull(sseHelper);
    }

    @After public void tearDown() {
        sseHelper.stop();
    }

    @Test public void testEiffelSubscriber() throws Exception {
        final List<String> msgs = new ArrayList<>();

        String msg0 = "test-msg-1";
        String msg1 = "test-msg-2";
        String routingKey = "333";

        sseHelper.receive(port, routingKey, msgs);
        Thread.sleep(2000);
        rmqHelper.send(routingKey, msg0);
        rmqHelper.send(routingKey, msg1);
        Thread.sleep(8000);

        Assert.assertEquals(2, msgs.size());
        Assert.assertEquals(msg0, msgs.get(0));
        Assert.assertEquals(msg1, msgs.get(1));

    }

}
