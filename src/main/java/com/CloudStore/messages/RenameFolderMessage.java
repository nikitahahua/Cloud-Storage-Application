package com.CloudStore.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenameFolderMessage implements Message, Serializable {
    private String newFolderName;
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
