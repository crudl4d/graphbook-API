package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.Comment;
import com.dogebook.entities.Post;
import com.dogebook.entities.User;
import com.dogebook.repositories.CommentRepository;
import com.dogebook.repositories.PostRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/comments")
public class CommentResource {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Comment>> getComments(Long postId, @RequestParam @NotNull Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(commentRepository.findAll(pageable).getContent());
    }

    @PostMapping
    ResponseEntity<Void> writeComment(@RequestBody @Valid Comment comment, Principal principal) throws URISyntaxException {
        comment.setAuthor(userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow());
        comment.setCreated(Date.from(Instant.now()));
        Long id = commentRepository.save(comment).getId();
        return ResponseEntity.created(new URI("/comments/" + id)).build();
    }

    @GetMapping(value = "/{commentsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Post> getPost(@PathVariable Long commentId) {
        return ResponseEntity.ok(postRepository.findById(commentId).orElseThrow());
    }

    @PostMapping("/{postId}/like")
    ResponseEntity<Void> likeComment(@PathVariable("commentId") Long commentId, Principal principal) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        User user = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        if (!comment.getLikedBy().contains(user)) {
            comment.getLikedBy().add(user);
            comment.setLikes(comment.getLikes() == null ? 1L : comment.getLikes() + 1);
        }
        commentRepository.save(comment);
        return ResponseEntity.ok().build();
    }
}