package com.dogebook.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.DateLong;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("Post")
@Data
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    @NotBlank
    private String content;
    @Property
    private Long likes;
    @Property
    private byte[] image;
    @Property
    private User author;
    @Property
    private LocalDateTime created;

    @Relationship(type = "LIKED_BY", direction = OUTGOING)
    private List<User> likedBy;
    @Relationship("COMMENT_ON_POST")
    private List<Comment> comments;
}
