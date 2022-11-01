package com.dogebook.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.DateLong;

import java.util.Date;
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
    @DateLong
    private Date birthDate;
    @Property
    private String profilePicturePath;

    @Relationship(type = "IS_FRIENDS_WITH", direction = OUTGOING)
    private Set<User> friends;
}