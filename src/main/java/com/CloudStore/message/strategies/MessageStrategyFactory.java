package com.CloudStore.message.strategies;

import com.CloudStore.exceptions.EntityNotFoundException;
import com.CloudStore.messages.DeleteObjectMessage;
import com.CloudStore.messages.Message;
import com.CloudStore.messages.RenameFolderMessage;
import com.CloudStore.messages.UploadFileMessage;
import com.CloudStore.service.S3Service;
import org.springframework.stereotype.Component;

@Component
public class MessageStrategyFactory {

    private final S3Service s3Service;

    public MessageStrategyFactory(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public MessageStrategy getStrategy(Message message){
        if (message instanceof DeleteObjectMessage){
            return new DeleteStrategy(s3Service);
        } else if (message instanceof UploadFileMessage) {
            return new UploadFileStrategy(s3Service);
        } else if (message instanceof RenameFolderMessage){
            return new RenameFileStrategy(s3Service);
        }
        else {
            throw new EntityNotFoundException("cant find entity "+ message.getClass());
        }
    }

}
