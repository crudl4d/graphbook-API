package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.User;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/me")
public class PrincipalResource {

    public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    private UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> getLoggedInUser(Principal principal) {
        UserContext userContext = ((UserContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
        return ResponseEntity.ok(userRepository.findById(userContext.getId()).orElseThrow());
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<Void> uploadImage(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename());
        Files.write(fileNameAndPath, image.getBytes());
        String fileUrl = userRepository.postProfilePicture(UserContext.getUser(principal).getId(), fileNameAndPath.toString()).getProfilePicturePath();
        return ResponseEntity.ok().header("Location", fileUrl).build();
    }

    @GetMapping(value = "/profile-picture", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(Principal principal) throws IOException {
        User user = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        Path fileNameAndPath = Paths.get(user.getProfilePicturePath());
        return ResponseEntity.ok(Files.readAllBytes(fileNameAndPath));
    }
}