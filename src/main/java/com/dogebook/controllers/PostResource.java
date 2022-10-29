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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostResource {

    private PostRepository postRepository;
    private UserRepository userRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Post>> getPosts(@RequestParam @NotNull Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(postRepository.findAll(pageable).getContent());
    }

    @PostMapping
    ResponseEntity<Void> createPost(@RequestBody @Valid Post post, Principal principal) throws URISyntaxException {
        post.setAuthor(userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow());
        post.setCreated(Date.from(Instant.now()));
        Long id = postRepository.save(post).getId();
        return ResponseEntity.created(new URI("/posts/" + id)).build();
    }

    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Post> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postRepository.findById(postId).orElseThrow());
    }

    @PostMapping("/{postId}/like")
    ResponseEntity<Void> likePost(@PathVariable("postId") Long postId, Principal principal) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        if (!post.getLikedBy().contains(user)) {
            post.getLikedBy().add(user);
            post.setLikes(post.getLikes() == null ? 1L : post.getLikes() + 1);
        }
        postRepository.save(post);
        return ResponseEntity.ok().build();
    }
}