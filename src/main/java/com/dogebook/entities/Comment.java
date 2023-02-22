package com.dogebook.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("Comment")
@Data
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    @NotBlank
    private String content;
    @Property
    private Long likes;
    @Property
    private User author;
    @Property
    private LocalDateTime created;

    @Relationship(type = "LIKED_BY", direction = OUTGOING)
    private List<User> likedBy;
}
