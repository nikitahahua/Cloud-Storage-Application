package com.CloudStore.message.strategies;

import com.CloudStore.messages.DeleteObjectMessage;
import com.CloudStore.messages.Message;
import com.CloudStore.service.S3Service;
import lombok.SneakyThrows;

public class DeleteStrategy implements MessageStrategy{

    private final S3Service s3Service;

    public DeleteStrategy(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @SneakyThrows
    @Override
    public void processMessage(Message message) {
        DeleteObjectMessage deleteObjectMessage = (DeleteObjectMessage) message;
        if (deleteObjectMessage.isFolder()){
            s3Service.deleteFolderRecursive(message.getUserId(), message.getPath());
        }
        else{
            s3Service.deleteObject(deleteObjectMessage.getPath(), message.getUserId());
        }
    }
}
