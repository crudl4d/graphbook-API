package com.dogebook.controllers;

import com.dogebook.PrincipalService;
import com.dogebook.configuration.UserContext;
import com.dogebook.entities.User;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/me")
public class PrincipalResource {

    private UserRepository userRepository;

    @Autowired
    private PrincipalService principalService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> getLoggedInUser(Principal principal) {
        UserContext userContext = ((UserContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
        return ResponseEntity.ok(userRepository.findById(userContext.getId()).orElseThrow());
    }

    @PatchMapping
    ResponseEntity<Void> editUser(Principal principal, @RequestBody @Valid User user) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        User oldUser = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        userRepository.save(principalService.patchUser(oldUser, user));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<Void> uploadImage(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        return ResponseEntity.ok().header("Location", principalService.createFile(image, principal)).build();
    }

    @GetMapping(value = "/profile-picture", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(Principal principal) throws IOException {
        User user = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        Path fileNameAndPath = Paths.get(user.getProfilePicturePath());
        return ResponseEntity.ok(Files.readAllBytes(fileNameAndPath));
    }
}