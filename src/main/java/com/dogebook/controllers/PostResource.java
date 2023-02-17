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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostResource {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Post>> getPosts(@RequestParam @NotNull Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "created"));
        List<Post> posts = postRepository.findAll(pageable).getContent();
        posts = posts.stream().filter(post -> Post.Visibility.PUBLIC.toString().equals(post.getVisibility())).toList();
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody @Valid Post post, Principal principal) throws URISyntaxException {
        post.setAuthor(userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow());
        post.setLikes(0L);
        post.setCreated(LocalDateTime.now());
        Long id = postRepository.save(post).getId();
        return ResponseEntity.created(new URI("/posts/" + id)).build();
    }

    @GetMapping(value = "/friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Post>> getFriendsPosts(Principal principal) {
        List<Post> posts = postRepository.findFriendsPosts(UserContext.getUser(principal).getId());
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postRepository.findById(postId).orElseThrow());
    }

    @DeleteMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable("postId") Long postId, Principal principal) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        if (!post.getLikedBy().contains(user)) {
            post.getLikedBy().add(user);
            post.setLikes(post.getLikes() == null ? 1L : post.getLikes() + 1);
        }
        postRepository.save(post);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{postId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postRepository.findById(postId).orElseThrow().getComments());
    }

    @PostMapping(value = "/{postId}/comments")
    public ResponseEntity<Void> writeComment(@PathVariable Long postId, @RequestBody @Valid Comment comment, Principal principal) throws URISyntaxException {
        comment.setAuthor(userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow());
        comment.setCreated(LocalDateTime.now());
        Long commentId = commentRepository.save(comment).getId();
        postRepository.addCommentToPost(postId, commentId);
        return ResponseEntity.created(new URI("/posts/" + postId + "comments/" + commentId)).build();
    }

    @GetMapping(value = "/{postId}/comments/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> getComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentRepository.findById(commentId).orElseThrow());
    }

    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<Void> likeComment(@PathVariable("commentId") Long commentId, Principal principal) {
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