package com.dogebook.controllers;

import com.dogebook.entities.FriendRequest;
import com.dogebook.entities.Post;
import com.dogebook.entities.User;
import com.dogebook.repositories.FriendRequestRepository;
import com.dogebook.repositories.PostRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostResource {

    private final PostRepository postRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Post>> getPosts(Principal principal) {
        return ResponseEntity.ok(postRepository.findAll());
    }

}