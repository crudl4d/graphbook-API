package com.dogebook.repositories;

import com.dogebook.entities.Comment;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface CommentRepository extends Neo4jRepository<Comment, Long> {
    @NotNull
    @Query(value = "MATCH (n:Comment) where n.created is not null RETURN n order by n.created desc LIMIT 25", countQuery = "MATCH (n:Comment) RETURN count(n)")
    Page<Comment> findAll(Pageable pageable);
}
