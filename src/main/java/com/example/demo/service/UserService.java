package com.example.demo.service;

import com.example.demo.config.S3Bucket;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.module.User;
import com.example.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final S3Service s3Service;
    private final S3Bucket s3Bucket;

    public UserResponseDto getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponseUser(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepo.findAll().stream().map(userMapper::toResponseUser).toList();
    }

    public void uploadProfileImage(Long userId, MultipartFile file) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        System.out.println(s3Bucket.getCustomer());
        try {
            var profileImage = UUID.randomUUID().toString();
            s3Service.putObject(
                    "java-s3-bucket-test",
                    "profile/images/%s/%s/".formatted(userId, profileImage),
                    file.getBytes()
            );

            user.setProfilePicture(profileImage);
            userRepo.save(user);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }
    public byte[] getUserProfileImage(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        return s3Service.getObject(
                "java-s3-bucket-test",
                "profile/images/%s/%s/".formatted(user.getId(), user.getProfilePicture())

        );

    }

    public void deleteUserProfileImage(Long userId) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        s3Service.deleteObject(
                "java-s3-bucket-test",
                "profile/images/%s/%s/".formatted(user.getId(), user.getProfilePicture())
        );
        user.setProfilePicture("");
        userRepo.save(user);

    }

    public void updateUserProfileImage(Long userId, MultipartFile file) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        try {
            s3Service.deleteObject(
                    "java-s3-bucket-test",
                    "profile/images/%s/%s/".formatted(user.getId(), user.getProfilePicture())
            );
            s3Service.putObject(
                    "java-s3-bucket-test",
                    "profile/images/%s/%s/".formatted(user.getId(), user.getProfilePicture()),
                    file.getBytes()
            );
        } catch (Exception e) {
            throw new IllegalStateException("User not found");
        }

    }
}
