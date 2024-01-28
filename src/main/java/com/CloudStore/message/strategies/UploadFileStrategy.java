package com.CloudStore.message.strategies;

import com.CloudStore.messages.Message;
import com.CloudStore.messages.UploadFileMessage;
import com.CloudStore.model.FileMessage;
import com.CloudStore.service.S3Service;

public class UploadFileStrategy implements MessageStrategy{

    private final S3Service s3Service;

    public UploadFileStrategy(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Override
    public void processMessage(Message message) {
        UploadFileMessage uploadFileMessage = (UploadFileMessage) message;
        s3Service.uploadFile(uploadFileMessage.getUserId(), uploadFileMessage.getPath(), uploadFileMessage.getFile());
    }
}
