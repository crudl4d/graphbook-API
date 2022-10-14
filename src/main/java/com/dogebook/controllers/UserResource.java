package com.dogebook.controllers;

import com.dogebook.entities.User;
import com.dogebook.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserResource {

    private final UserRepository userRepository;
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    ResponseEntity login(Principal principal) {
        return new ResponseEntity(Map.of("", ""), HttpStatus.OK);
    }
    @PostMapping
    ResponseEntity<Void> createUser(@RequestBody User user) throws URISyntaxException {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        Long id = userRepository.save(user).getId();
        return ResponseEntity.created(new URI("/users/" + id)).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<User>> getUsers(Principal principal) {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/{userId}")
    ResponseEntity<Void> update(@RequestBody User user, @PathVariable("userId") Long userId) {
        user.setId(userId);
        userRepository.save(user);
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

//    @ResponseBody
//    @GetMapping(value = "{title}/directors", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<Set<User>> getDirectors(@PathVariable String title) {
//        return ResponseEntity.ok(userRepository.findOneByTitle(title).block().getDirectors());
//    }
}