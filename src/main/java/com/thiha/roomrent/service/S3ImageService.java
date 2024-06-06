package com.thiha.roomrent.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thiha.roomrent.exceptions.S3ImageUploadException;


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
        try{
            amazonS3.putObject(putObjectRequest);
        }catch(AmazonServiceException e){
            switch (e.getStatusCode()) {
               case 400:
                     throw new S3ImageUploadException("Bad Request "+e.getErrorMessage());
               case 403:
                     throw new S3ImageUploadException("Access Denied ");
               case 404:
                     throw new S3ImageUploadException("Bucket or key not found");
               case 500:
                     throw new S3ImageUploadException("Internal Server Error on AWS S3 ");
               case 503:
                     throw new S3ImageUploadException("AWS S3 service unavaliable");
               default:
                  throw new S3ImageUploadException("Error occured while uploading image to AWS S3");
            }
         }catch(SdkClientException e){
            System.out.println("cannot connect to s3");
            throw new S3ImageUploadException("Cannot connect to s3");
         }
   }

   public void deleteImage(String key){
      DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
      amazonS3.deleteObject(deleteObjectRequest);
   }
}

