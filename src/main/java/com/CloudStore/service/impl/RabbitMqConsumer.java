package com.CloudStore.service.impl;

import com.CloudStore.message.strategies.MessageStrategyFactory;
import com.CloudStore.messages.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.minio.errors.MinioException;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMqConsumer {

    private final MessageStrategyFactory messageStrategy;
    public RabbitMqConsumer(MessageStrategyFactory messageStrategy) {
        this.messageStrategy = messageStrategy;
    }

    @RabbitListener(queues = "file-queue")
    public void processMessage(Message message) throws MinioException, JsonProcessingException {
        messageStrategy.getStrategy(message).processMessage(message);
    }

}
