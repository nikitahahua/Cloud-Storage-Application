package com.CloudStore.service;

import com.CloudStore.messages.DeleteObjectMessage;
import com.CloudStore.messages.RenameFolderMessage;
import com.CloudStore.messages.UploadFileMessage;

public interface RabbitMqProducerService {
    void sendUploadMessage(UploadFileMessage uploadFileMessage);
    void sendDeleteMessage(DeleteObjectMessage deleteObjectMessage);
    void sendRenameMessage(RenameFolderMessage renameFolderMessage);
}
