package com.dogebook.repositories;

import com.dogebook.entities.Comment;
import com.dogebook.entities.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CommentRepository extends Neo4jRepository<Comment, Long> {
    @NotNull Page<Comment> findAll(Pageable pageable);
}
