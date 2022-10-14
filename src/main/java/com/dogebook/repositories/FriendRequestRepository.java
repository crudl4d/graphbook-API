package com.dogebook.repositories;

import com.dogebook.entities.FriendRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface FriendRequestRepository extends Neo4jRepository<FriendRequest, Long> {
}
