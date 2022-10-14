package com.dogebook.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("User")
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private LocalDate birthDate;

    @Relationship(type = "IS_FRIENDS_WITH", direction = OUTGOING)
    private Set<User> friends = new HashSet<>();
    @Relationship(type = "AUTHORED", direction = OUTGOING)
    private Set<Post> posts = new HashSet<>();
}