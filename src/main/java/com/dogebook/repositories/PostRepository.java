package com.dogebook.repositories;

import com.dogebook.entities.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PostRepository extends Neo4jRepository<Post, Long> {
    @NotNull Page<Post> findAll(Pageable pageable);
}
