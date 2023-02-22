package test;

import com.dogebook.controllers.PostResource;
import com.dogebook.entities.Comment;
import com.dogebook.entities.Post;
import com.dogebook.entities.User;
import com.dogebook.repositories.CommentRepository;
import com.dogebook.repositories.PostRepository;
import com.dogebook.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostResourceTest {

    private final static Long POST_LIKES = 10L;
    private final static Long COMMENT_LIKES = 5L;
    private final static Long ID = 0L;
    PostRepository postRepository;

    UserRepository userRepository;

    CommentRepository commentRepository;
    @InjectMocks
    PostResource postResource;

    @BeforeAll
    public void beforeAll() {
        postRepository = mock(PostRepository.class);
        commentRepository = mock(CommentRepository.class);
        userRepository = mock(UserRepository.class);
        var comment = new Comment(ID, "content", POST_LIKES, null, LocalDateTime.now(), null);
        var post = new Post(ID, "content", COMMENT_LIKES, null, null, null, null, List.of(comment));
        var user = new User(ID, "name", "surname", null, null, null, null, null, null);
        Mockito.when(postRepository.findById(ID)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        Mockito.when(postRepository.findAll()).thenReturn(List.of(post));
        Mockito.when(postRepository.count()).thenReturn(1L);
    }

    @Test
    void getPost() {
        assertEquals(COMMENT_LIKES, postResource.getPost(ID).getBody().getLikes());
    }

    @Test
    void given_commentIsWrittenOnPost_when_postIsFetched_then_postContainsComment() throws URISyntaxException {
//        var commentToWrite = commentRepository.findById(COMMENT_ID).orElseThrow(new ChangeSetPersister.NotFoundException());
//        postResource.writeComment(POST_ID, commentToWrite);
//        assertEquals(POST_LIKES, postResource.getCommentsForPost(ID, 0).getBody().contains(commentToWrite));
    }

    @Test
    void getCommentsForPost() {
        assertEquals(POST_LIKES, postResource.getPost(ID).getBody().getComments().get(0).getLikes());
    }
}