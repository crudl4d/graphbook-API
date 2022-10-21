package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.Post;
import com.dogebook.entities.User;
import com.dogebook.repositories.PostRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostResource {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok(postRepository.findAll());
    }

    @PostMapping("/{postId}/like")
    ResponseEntity<Void> likePost(@PathVariable("postId") Long postId, Principal principal) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(((UserContext) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getId()).orElseThrow();
        if (!post.getLikedBy().contains(user)) {
            post.getLikedBy().add(user);
        }
        postRepository.save(post);
        return ResponseEntity.ok().build();
    }
}