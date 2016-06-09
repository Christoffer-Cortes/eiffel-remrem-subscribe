package com.ericsson.eiffel.remrem.subscribe.util;

import com.rabbitmq.client.Channel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(MockitoJUnitRunner.class) public class ConnectionHelperTest {

    private ConnectionHelper unitUnderTest;
    @Mock private SseEmitter mockEmitter;
    @Mock private Channel mockChannel;
    private static final String QUEUE_NAME = "some-queue";

    @Before public void setup() throws IOException, TimeoutException {
        unitUnderTest = new ConnectionHelper();
        Mockito.doNothing().when(mockEmitter).completeWithError(Mockito.any(Throwable.class));
        Mockito.when(mockChannel.isOpen()).thenReturn(false);
        Mockito.doNothing().when(mockChannel).close();
    }

    @Test public void testOnQueueCancelled() throws Exception {
        unitUnderTest.onQueueCancelled(mockEmitter, mockChannel, QUEUE_NAME);

        Mockito.verify(mockEmitter, Mockito.times(1))
            .completeWithError(Mockito.any(Throwable.class));
    }

}
