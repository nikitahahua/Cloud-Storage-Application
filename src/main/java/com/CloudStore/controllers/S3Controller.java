package com.CloudStore.controllers;

import com.CloudStore.exceptions.FileNotFoundException;
import com.CloudStore.messages.DeleteObjectMessage;
import com.CloudStore.model.User;
import com.CloudStore.service.RabbitMqProducerService;
import com.CloudStore.service.S3Service;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class S3Controller {

    private final S3Service fileService;
    private final RabbitMqProducerService producerService;
    private final UserDetailsService userDetailsService;
    public S3Controller(S3Service fileService, RabbitMqProducerService producerService, UserDetailsService userDetailsService) {
        this.fileService = fileService;
        this.producerService = producerService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("create-folder")
    public ResponseEntity<String> createFolder(@RequestParam("folder-name") String folderName, @RequestParam("path") String path, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loadedUser = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
        Long userId = loadedUser.getId();
        fileService.createFolder(folderName, path, userId);
        return new ResponseEntity<>("successfully created folder by path: "+ path, HttpStatus.OK);
    }

    @DeleteMapping("delete-folder")
    public ResponseEntity<String> deleteFolder(@RequestParam("path") String path, Authentication authentication) throws MinioException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loadedUser = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
        Long userId = loadedUser.getId();
        DeleteObjectMessage deleteObjectMessage = new DeleteObjectMessage(true, userId, path);
        producerService.sendDeleteMessage(deleteObjectMessage);
        return new ResponseEntity<>("successfully deleted folder by path: "+ path, HttpStatus.OK);
    }

    @DeleteMapping("delete-file")
    public ResponseEntity<String> deleteFile(@RequestParam("path") String path, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loadedUser = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
        Long userId = loadedUser.getId();
        DeleteObjectMessage deleteObjectMessage = new DeleteObjectMessage(false, userId, path);
        producerService.sendDeleteMessage(deleteObjectMessage);
        return new ResponseEntity<>("successfully deleted file by path: "+ path, HttpStatus.OK);
    }

    @PutMapping("upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("path") String path,@RequestParam("file") MultipartFile file, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loadedUser = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
        Long userId = loadedUser.getId();
        fileService.uploadFile(userId, path, file);
        return new ResponseEntity<>("successfully uploaded file by path: "+ path, HttpStatus.OK);
    }

    @GetMapping("/download-file")
    public void downloadFile(@RequestParam("path") String path, HttpServletResponse response, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loadedUser = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
        Long userId = loadedUser.getId();

        try {
            var downloadName = URLEncoder.encode(path.substring(path.lastIndexOf("/") + 1), StandardCharsets.UTF_8);
            var fileStream = fileService.downloadFile(path, userId);

            response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadName + "\"");
            response.setContentType("application/octet-stream");
            FileCopyUtils.copy((InputStream) fileStream, response.getOutputStream());
        } catch (FileNotFoundException e) {
            log.error("File not found: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            log.error("Error with downloading file: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rename-obj")
    public ResponseEntity<String> renameObject(@RequestParam("path") String path, @RequestParam("newName") String newName, Authentication authentication) throws MinioException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loadedUser = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
        Long userId = loadedUser.getId();

        fileService.renameFolder(path, newName, userId);
        return new ResponseEntity<>("successfully renamed file", HttpStatus.OK);
    }

}
