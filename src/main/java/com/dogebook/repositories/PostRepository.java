package com.dogebook.repositories;

import com.dogebook.entities.Post;
import com.dogebook.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

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

    @Query("MATCH (p:Post)-[AUTHOR]->(u:User) WHERE id(u)=$userId return p")
    List<Post> findPosts(Long userId);

    @Query(value = "MATCH (u1)-[f:IS_FRIENDS_WITH {accepted: true}]-(u), (p:Post)-[AUTHOR]->(u1) WHERE id(u)=$userId RETURN p")
    List<Post> findFriendsPosts(Long userId);

    @Query("MATCH (p:Post)-[:AUTHOR]-(u:User) WHERE id(p)=$postId return id(u)")
    Long findPostAuthor(Long postId);
}
