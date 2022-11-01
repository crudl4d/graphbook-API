package com.dogebook.repositories;

import com.dogebook.entities.Post;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
public interface PostRepository extends Neo4jRepository<Post, Long> {
    @NotNull
//    @Query(value = "MATCH (n:Post) where n.created is not null RETURN n order by n.created desc LIMIT 25", countQuery = "MATCH (n:Post) RETURN count(n)")
    Page<Post> findAll(@NotNull Pageable pageable);

    @Query("""
            MATCH
              (p:Post),
              (c:Comment)
            WHERE id(p)=$postId AND id(c)=$commentId
            CREATE (p)-[r:COMMENT_ON_POST]->(c)
            """)
    void addCommentToPost(Long postId, Long commentId);
}
