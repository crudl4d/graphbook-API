package com.dogebook.repositories;

import com.dogebook.entities.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long> {
    List<User> findAll();
    User findByEmail(String email);
}
