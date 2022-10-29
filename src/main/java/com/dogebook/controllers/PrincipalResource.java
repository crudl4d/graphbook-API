package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.Post;
import com.dogebook.entities.User;
import com.dogebook.repositories.PostRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/me")
public class PrincipalResource {

    private UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> getLoggedInUser(Principal principal) {
        UserContext userContext = ((UserContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
        return ResponseEntity.ok(userRepository.findById(userContext.getId()).orElseThrow());
    }
}