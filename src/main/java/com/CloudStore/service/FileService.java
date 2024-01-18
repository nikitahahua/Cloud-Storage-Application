//package com.CloudStore.service;
//
//import com.CloudStore.model.User;
//import io.minio.BucketExistsArgs;
//import io.minio.MakeBucketArgs;
//import io.minio.errors.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import io.minio.MinioClient;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//
//
//@Service
//@Slf4j
//public class FileService {
//    private static final String ACCESS_KEY = "Q3AM3UQ867SPQQA43P2F";
//    private static final String SECRET_KEY = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";
//    private final MinioClient minioClient;
//
//    public FileService() {
//        this.minioClient =
//        MinioClient.builder()
//                .endpoint("https://play.min.io")
//                .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
//                .build();;
//    }
//
//    public void uploadFile(String path, MultipartFile file){
//
//    }
//
//    public void uploadFolder(String path){
//
//    }
//
//    public void createBucket(User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        String bucketName = "user"+" "+user.getId();
//        boolean found =
//                minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
//        if (!found) {
//            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
//            log.info(bucketName+" has been created");
//        } else {
//            log.warn(bucketName+" has NOT been created !!!");
//        }
//    }
//
//}
