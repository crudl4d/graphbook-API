package com.dogebook.entities;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Node("Post")
@Data
public class Post {
    @Id
    @GeneratedValue
    private final Long id;
    @Property
    private final String content;
    @Property
    private final byte[] image;

    @Relationship(type = "AUTHORED", direction = INCOMING)
    private final User author;
}
