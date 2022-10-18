package com.dogebook.repositories;

import com.dogebook.entities.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface PostRepository extends Neo4jRepository<Post, Long> {
    List<Post> findAll();
}
