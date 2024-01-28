package com.CloudStore.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileMessage implements Message, Serializable {
    private MultipartFile file;
    private Long userId;
    private String path;

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Long getUserId() {
        return userId;
    }
}