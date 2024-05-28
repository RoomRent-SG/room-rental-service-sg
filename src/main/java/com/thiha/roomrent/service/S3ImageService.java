package com.thiha.roomrent.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Service
public class S3ImageService {
   @Autowired
   private AmazonS3 amazonS3;

   @Value("${aws.s3.bucketName}")
   private String bucketName;

   public void uploadImage(String key, MultipartFile file) throws IOException{
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getBytes().length);
        // to view in browser instead of downloading while accessing from cloudfront
        metadata.setContentType("image/png");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
        amazonS3.putObject(putObjectRequest);
   }

   public void deleteImage(String key){
      DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
      amazonS3.deleteObject(deleteObjectRequest);
   }
}

