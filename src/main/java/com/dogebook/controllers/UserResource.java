package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.User;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserResource {

    private UserRepository userRepository;

    @PostMapping("/login")
    ResponseEntity<Map<?, ?>> login(Principal principal) {
        UserContext userContext = ((UserContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());
        HttpHeaders headers = new HttpHeaders();
        headers.add("id", userContext.getId().toString());
        headers.add("name", userContext.getName());
        headers.add("email", userContext.getEmail());
        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Void> registerUser(@RequestBody User user) throws URISyntaxException {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Long id = userRepository.save(user).getId();
        return ResponseEntity.created(new URI("/users/" + id)).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<User>> getUsers(@RequestParam @NotNull Integer page, Principal principal) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(userRepository.findAll(pageable).getContent());
    }

    @PutMapping("/{userId}")
    ResponseEntity<Void> update(@RequestBody User user, @PathVariable("userId") Long userId) {
        if (userRepository.existsById(userId)) {
            user.setId(userId);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Optional<User>> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(userRepository.findById(userId));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Void> delete(@PathVariable Long userId) {
        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}