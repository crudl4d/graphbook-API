package com.dogebook.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.*;

@Getter
@Setter
@RelationshipProperties
public class Friend {

    @RelationshipId
    @GeneratedValue
    private Long id;
    @TargetNode
    private final User user;

    @Property
    private Long invitingUserId;

    public Friend(User user, Long invitingUserId, Boolean accepted) {
        this.user = user;
        this.invitingUserId = invitingUserId;
        this.accepted = accepted;
    }

    private Boolean accepted = false;
}