package com.dogebook.repositories;

import com.dogebook.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("MATCH (u:User) WHERE id(u)=$userId SET u.profilePicturePath=$profilePicturePath RETURN u")
    User postProfilePicture(Long userId, String profilePicturePath);

    @Query("MATCH (u:User) WHERE u.firstName+' '+u.surname=~$regex return u")
    List<User> findByName(String regex);

    @Query("MATCH (u:User), (u1:User) WHERE id(u)=$invitedUserId and id(u1)=$invitingUserId CREATE (u)-[:IS_FRIENDS_WITH { accepted: false }]->(u1)")
    void addFriend(Long invitedUserId, Long invitingUserId);
}
