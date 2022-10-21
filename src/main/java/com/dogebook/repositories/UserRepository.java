package com.dogebook.repositories;

import com.dogebook.entities.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
