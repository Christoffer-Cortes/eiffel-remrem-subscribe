package com.ericsson.eiffel.remrem.subscribe.util;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Component("connectionHelper") @Slf4j public class ConnectionHelper {

    private ConcurrentHashMap<SseEmitter, ChannelInfo> map = new ConcurrentHashMap();

    public void onEmitterStarted(SseEmitter emitter) {
        log.debug("onEmitterStarted");
    }

    public void onQueueStarted(SseEmitter sseEmitter, Channel ch, String queueName) {
        log.debug("onQueueStarted");
        map.put(sseEmitter, new ChannelInfo(ch, queueName));
    }

    public void onQueueCancelled(SseEmitter sseEmitter, Channel ch, String queueName) {
        log.debug("onQueueCancelled");
        sseEmitter.completeWithError(new Exception("Consumer Cancelled on RabbitMQ end"));
        cleanUp(sseEmitter, ch, queueName);
    }

    private void cleanUp(SseEmitter sseEmitter, Channel ch, String queueName) {
        log.debug("closeChannel: " + ch.getChannelNumber() + " and queue: " + queueName);
        if (ch == null || !ch.isOpen()) {
            return;
        }
        try {
            ch.close();
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        } finally {
            map.remove(sseEmitter);
        }
    }

    public void onEmitterCompleted(SseEmitter emitter) {
        log.debug("onEmitterCompleted");
        ChannelInfo info = map.get(emitter);
        if (info != null) {
            cleanUp(emitter, info.ch, info.queueName);
        }
    }

    private class ChannelInfo {
        Channel ch;
        String queueName;

        public ChannelInfo(Channel ch, String queueName) {
            this.ch = ch;
            this.queueName = queueName;
        }
    }
}
