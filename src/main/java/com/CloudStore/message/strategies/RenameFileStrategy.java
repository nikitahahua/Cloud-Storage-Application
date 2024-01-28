package com.CloudStore.message.strategies;

import com.CloudStore.messages.Message;
import com.CloudStore.messages.RenameFolderMessage;
import com.CloudStore.service.S3Service;
import lombok.SneakyThrows;

public class RenameFileStrategy implements MessageStrategy{

    private final S3Service s3Service;

    public RenameFileStrategy(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @SneakyThrows
    @Override
    public void processMessage(Message message) {
        RenameFolderMessage renameFolderMessage = (RenameFolderMessage) message;
        s3Service.renameFolder(renameFolderMessage.getPath(), renameFolderMessage.getNewFolderName(), renameFolderMessage.getUserId());
    }
}
