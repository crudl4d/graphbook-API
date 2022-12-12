package com.dogebook.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;
import org.springframework.data.neo4j.core.support.DateLong;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("User")
@Data
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    @NotBlank
    private String firstName;
    @Property
    @NotBlank
    private String surname;
    @Property
    @NotBlank
    private String email;
    @Property
    @NotBlank
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
    private List<Friend> friends;
}