package com.CloudStore.service.impl;

import com.CloudStore.exceptions.FileNotFoundException;
import com.CloudStore.exceptions.FolderSecurityException;
import com.CloudStore.service.S3Service;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {
    private final MinioClient minioClient;

    public S3ServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Value("${minio.bucket.name}")
    private String rootBucket;

    @Override
    public InputStream downloadFile(String fullPath, Long id) {
        isUserFolder(fullPath, id);
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(rootBucket)
                            .object(fullPath)
                            .build()
            );
        } catch (Exception e){
            throw new FileNotFoundException("cant download file by path : " + fullPath);
        }
    }

    @SneakyThrows
    @Override
    public void uploadFile(Long userId, String path, MultipartFile file) {
        if (!path.startsWith("/")){
            path = "/"+path;
        }
        var fullPath = Paths.get(path, file.getOriginalFilename()).toString();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(fullPath).stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build());

            log.info("Uploaded file: " + file.getOriginalFilename());
        } catch (Exception e) {
            log.error(e.getMessage());

            throw new MinioException(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void deleteObject(String path, Long userId) {
        isUserFolder(path, userId);
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(rootBucket).object(path).build());
        log.info("Deleted object: " + path);
    }

    @SneakyThrows
    @Override
    public void createFolder(String folderName, String path, Long userId) {
        isUserFolder(path, userId);

        path = path.endsWith("/") ? path : path + "/";
        String fullFolderPath = path + folderName + "/";

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(rootBucket)
                    .object(fullFolderPath)
                    .stream(new ByteArrayInputStream(new byte[0]), 0, 0)
                    .build());
            log.info("Created folder: " + fullFolderPath);
        } catch (MinioException e) {
            log.error("Error creating folder in MinIO: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error: " + e.getMessage(), e);
            throw new RuntimeException("Error creating folder in MinIO", e);
        }
    }

    @Override
    public void deleteFolderRecursive(long userId, String rawPath) throws MinioException {
        try {
            var objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(rootBucket).recursive(true).prefix(rawPath).build());

            for (var result : objects) {
                var item = result.get();

                // If item is a directory or if the path is not a directory and the path is equal to the item name
                // then delete the item

                if (isPathOverrideObject(item.isDir(), rawPath, item.objectName())) {
                    deleteObject(item.objectName(), userId);

                    log.info("Deleted object: " + item.objectName());
                }
            }

            deleteObject(rawPath, userId);

            log.info("Deleted folder: " + rawPath);

        } catch (Exception e) {
            log.error(e.getMessage());

            throw new MinioException(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void createUserInitialFolder(Long id) {
        String userFolderName = "user-" + id + "-files/";
        createFolder(userFolderName, "/", id);
    }


    /*

    ToDo: i think im just gonna :
                                - download content from folder
                                - create new folder
                                - load content in new folder
                                - delete old folder

    */
    @Override
    public void renameFolder(String path, String newFolderName, Long userId) {
        try {
            Iterable<Result<Item>> items = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(rootBucket).prefix(path).recursive(true).build()
            );

            List<String> objectPaths = new ArrayList<>();
            items.forEach(item -> {
                try {
                    objectPaths.add(item.get().objectName());
                } catch (Exception e) {
                    log.error("Error listing object: " + e.getMessage());
                }
            });

            String newPath = Path.of(path).resolveSibling(newFolderName).toString();

            createFolder(newFolderName, newPath, userId);

            for (String objectPath : objectPaths) {
                String newObjectPath = objectPath.replace(path, newPath);
                try {
                    copyObject(objectPath, newObjectPath);
                } catch (Exception e) {
                    log.error("Error copying object from " + objectPath + " to " + newObjectPath + ": " + e.getMessage());
                }
            }
            deleteObject(path, userId);
        } catch (Exception e) {
            log.error("Error renaming folder: " + e.getMessage());
        }
    }

    private void isUserFolder(String path, Long userId) {
        String expectedFolderName = "user-" + userId + "-files";
        if (Objects.equals(path, "") || Objects.equals(path, "/")){
            return;
        }
        path = path.replace("/", "");
        boolean isUserFolder = path.startsWith(expectedFolderName);
        if (!isUserFolder) {
            throw new FolderSecurityException("Can't delete someone else's file, only yours.");
        }
    }

    @SneakyThrows
    private void copyObject(String sourcePath, String destPath) {
        minioClient.copyObject(
                CopyObjectArgs.builder()
                        .bucket(rootBucket)
                        .source(CopySource.builder().bucket(rootBucket).object(sourcePath).build())
                        .object(destPath)
                        .build()
        );
        log.info("Successfully copied object from " + sourcePath + " to " + destPath);
    }

    private Iterable<Result<Item>> findObjects(String name, String prefix, String startAfter) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(name)
                        .startAfter("")
                        .prefix(prefix)
                        .maxKeys(100)
                        .build());
    }

    private boolean isPathOverrideObject(boolean isDir, String path, String objectName) {
        return isDir || path.length() - 1 < objectName.lastIndexOf("/") || objectName.equals(path);
    }

    private void removeListObjects(String bucketName, List<DeleteObject> objects) throws MinioException {
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                        RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
        for (Result<DeleteError> result : results) {
            DeleteError error;
            try {
                error = result.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            log.warn("Error in deleting object " + error.objectName() + "; " + error.message());
            throw new MinioException("Error in deleting object " + error.objectName() + "; " + error.message());
        }
    }


}
