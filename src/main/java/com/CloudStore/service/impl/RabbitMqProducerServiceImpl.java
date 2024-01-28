package com.CloudStore.service.impl;

import com.CloudStore.messages.DeleteObjectMessage;
import com.CloudStore.messages.RenameFolderMessage;
import com.CloudStore.messages.UploadFileMessage;
import com.CloudStore.service.RabbitMqProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RabbitMqProducerServiceImpl implements RabbitMqProducerService {

    private RabbitTemplate rabbitTemplate;
    private final TopicExchange topicExchange;

    public RabbitMqProducerServiceImpl(RabbitTemplate rabbitTemplate, TopicExchange topicExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchange = topicExchange;
    }

    @SneakyThrows
    @Override
    public void sendUploadMessage(UploadFileMessage uploadFileMessage) {
        log.info("Json message sent ---> {}", uploadFileMessage.toString());
        rabbitTemplate.convertAndSend(topicExchange.getName(), "file.upload", uploadFileMessage);
    }

    @SneakyThrows
    @Override
    public void sendDeleteMessage(DeleteObjectMessage deleteObjectMessage) {
        log.info("Json message sent ---> {}", deleteObjectMessage.toString());
        String topic = deleteObjectMessage.isFolder() ? "file.delete.folder" : "file.delete.file";
        rabbitTemplate.convertAndSend(topicExchange.getName(), topic, deleteObjectMessage);
    }

    @SneakyThrows
    @Override
    public void sendRenameMessage(RenameFolderMessage renameFolderMessage) {
        log.info("Json message sent ---> {}", renameFolderMessage.toString());
        rabbitTemplate.convertAndSend(topicExchange.getName(), "file.rename", renameFolderMessage);
    }
}
