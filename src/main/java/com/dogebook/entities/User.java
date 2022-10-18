package com.dogebook.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("User")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    @NotNull
    private String firstName;
    @Property
    @NotNull
    private String surname;
    @Property
    @NotNull
    private String email;
    @Property
    @NotNull
    private String password;
    @Property
    private Set<String> role;
    @Property
    @NotNull
    @DateTimeFormat
    private LocalDate birthDate;

    @Relationship(type = "IS_FRIENDS_WITH", direction = OUTGOING)
    private Set<User> friends;
    @Relationship(type = "AUTHORED", direction = OUTGOING)
    private Set<Post> posts;
}