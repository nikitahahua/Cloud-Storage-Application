package com.CloudStore.service;

import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    Object downloadFile(String fullPath, Long id);
    void uploadFile(Long userId, String rawFullPath, MultipartFile file);
    void deleteObject(String path, Long userId) ;
    void deleteFolderRecursive(long userId, String rawPath) throws MinioException;
    void createUserInitialFolder(Long id);
    void renameFolder(String path, String newFolderName,  Long userId) throws MinioException;
    void createFolder (String folderName, String path, Long userId);
}
