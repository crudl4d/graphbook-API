package com.dogebook.repositories;

import com.dogebook.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
