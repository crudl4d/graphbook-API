package com.dogebook.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

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
    private String content;
    @Property
    private Long likes;
    @Property
    private byte[] image;

    @Relationship(type = "AUTHORED", direction = INCOMING)
    private User author;
    @Relationship(type = "LIKED_BY", direction = OUTGOING)
    private List<User> likedBy;
}
