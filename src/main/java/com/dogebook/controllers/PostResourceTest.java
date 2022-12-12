package com.dogebook.controllers;

import com.dogebook.entities.Comment;
import com.dogebook.entities.Post;
import com.dogebook.repositories.PostRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostResourceTest {

    PostRepository postRepository;

    @InjectMocks
    PostResource postResource;

    @BeforeAll
    public void beforeAll() {
        postRepository = mock(PostRepository.class);
        var comment = new Comment(1L, "content", 10L, null, null, null);
        Post post = new Post(0L, "content", 5L, null, null, null, null, List.of(comment));
        Mockito.when(postRepository.findById(0L)).thenReturn(Optional.of(post));
        Mockito.when(postRepository.findAll()).thenReturn(List.of(post));
        Mockito.when(postRepository.count()).thenReturn(1L);
    }

    @Test
    void getPost() {
        assertEquals(postResource.getPost(0L).getBody().getLikes(), 5L);
    }

    @Test
    void getCommentsForPost() {
        assertEquals(postResource.getPost(0L).getBody().getComments().get(0).getLikes(), 10L);
    }
}