package com.dogebook.repositories;

import com.dogebook.entities.Friend;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FriendRequestRepository extends Neo4jRepository<Friend, Long> {
}
