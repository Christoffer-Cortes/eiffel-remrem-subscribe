package com.ericsson.eiffel.remrem.subscribe.message;

import com.ericsson.eiffel.remrem.subscribe.util.ConnectionHelper;
import com.ericsson.eiffel.remrem.subscribe.util.ContentHelper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

@Component("rabbitSender") @Slf4j
public class RabbitSender implements Sender {
    @Value("${rabbitmq.host}") private String host;
    @Value("${rabbitmq.exchange.name}") private String exchangeName;
    private Connection rabbitConnection;
    @Autowired private ConnectionHelper connectionHelper;
    @Autowired private ContentHelper contentHelper;

    @PostConstruct public void init() {
        log.info("RabbitSender init ...");
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            rabbitConnection = factory.newConnection();

        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override public void addEmitter(SseEmitter sseEmitter, String bindingKey)
        throws IOException {
        Channel ch = rabbitConnection.createChannel();
        String queueName = ch.queueDeclare().getQueue();
        ch.queueBind(queueName, exchangeName, bindingKey);
        log.debug("Binding key:" + bindingKey);
        Consumer consumer = new DefaultConsumer(ch) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                throws IOException {
                String message = new String(body, "UTF-8");
                log.debug(" [x] Received '" + message + "'");
                sseEmitter.send(contentHelper.getContent(message));
            }
            @Override
            public void handleCancel(String consumerTag) throws IOException {

                connectionHelper.onQueueCancelled(sseEmitter, ch, queueName);
            }
        };
        ch.basicConsume(queueName, true, consumer);
        connectionHelper.onQueueStarted(sseEmitter, ch, queueName);
    }

    @PreDestroy public void lastStep() throws IOException {
        log.info("RMQHelper lastStep ...");
        rabbitConnection.close();
    }

}
