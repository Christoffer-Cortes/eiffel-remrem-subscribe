package com.ericsson.eiffel.remrem.publish.sse.helper;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("rmqHelper") @Slf4j public class RMQHelper {

    @Value("${rabbitmq.host}") private String host;
    @Value("${rabbitmq.exchange.name}") private String exchangeName;

    public void send(String routingKey, String msg)
        throws IOException, InterruptedException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        log.debug("host" + host);
        factory.setHost(host);
        Connection rabbitConnection = factory.newConnection();

        Channel channel = rabbitConnection.createChannel();
        log.debug("host" + host);
        log.debug("exchangeName" + exchangeName);
        log.debug("routingKey" + routingKey);
        log.debug("msg" + msg);
        channel.basicPublish(exchangeName, routingKey, MessageProperties.BASIC, msg.getBytes());
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        channel.close();
        rabbitConnection.close();
    }
}
