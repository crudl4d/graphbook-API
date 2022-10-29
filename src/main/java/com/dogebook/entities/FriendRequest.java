package com.dogebook.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("FriendRequest")
@Data
@EqualsAndHashCode
public class FriendRequest {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private Long sendingUserId;
    @Property
    private Long invitedUserId;
    @Property
    private Boolean accepted = false;
}