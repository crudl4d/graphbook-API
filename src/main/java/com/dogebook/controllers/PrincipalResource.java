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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename().replaceAll("\\..*$", ".jpg"));
        Files.write(fileNameAndPath, image.getBytes());
        String fileUrl = userRepository.postProfilePicture(UserContext.getUser(principal).getId(), fileNameAndPath.toString()).getProfilePicturePath();

        BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(ImageIO.read(new ByteArrayInputStream(image.getBytes())), 0, 0, 100, 100, null);
        graphics2D.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        Files.write(Paths.get(UPLOAD_DIRECTORY, image.getOriginalFilename().replaceAll("\\..*$", "") + "-thumbnail.jpg"), bytes);

        return ResponseEntity.ok().header("Location", fileUrl).build();
    }

    @GetMapping(value = "/profile-picture", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(Principal principal) throws IOException {
        User user = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        Path fileNameAndPath = Paths.get(user.getProfilePicturePath());
        return ResponseEntity.ok(Files.readAllBytes(fileNameAndPath));
    }
}