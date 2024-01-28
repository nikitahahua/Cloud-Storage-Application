package com.CloudStore.messages;

import com.CloudStore.model.FileMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteObjectMessage implements Message, Serializable {
    private boolean isFolder;
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
