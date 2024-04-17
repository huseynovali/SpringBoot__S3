package com.example.demo.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public  void putObject(String bucketName, String key, byte[] file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file));
    }

    public byte[] getObject(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        ResponseInputStream<GetObjectResponse> res = s3Client.getObject(getObjectRequest);
        try {
            return res.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteObject(String bucketName, String key) {
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key));
    }

    public void uploadMultipleImages(String bucketName, String key, List<byte[]> files) {
        for (int i = 0; i < files.size(); i++) {
            putObject(bucketName, key + i, files.get(i));
        }
    }

}