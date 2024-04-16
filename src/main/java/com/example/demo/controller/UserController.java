package com.example.demo.controller;

import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    private final UserService userService;

    @GetMapping("all")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public UserResponseDto getUserById(Long id) {
        return userService.getUserById(id);
    }

    @GetMapping(value = "profile-image/{userId}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getUserProfileImage(@PathVariable("userId") Long userId) {
        return userService.getUserProfileImage(userId);
    }

    @PostMapping(value = "profile-image/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadProfileImage(@PathVariable("userId") Long userId,
                                   @PathVariable("file") MultipartFile file) {
        userService.uploadProfileImage(userId, file);
    }

    @DeleteMapping("profile-image/{userId}")
    public void deleteUserProfileImage(@PathVariable("userId") Long userId) {
        userService.deleteUserProfileImage(userId);
    }

    @PutMapping(value = "profile-image/{userId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateUserProfileImage(
            @PathVariable("userId") Long userId,
            @PathVariable("file") MultipartFile file
    ) {
        userService.updateUserProfileImage(userId, file);
    }


}
