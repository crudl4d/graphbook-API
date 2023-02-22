package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.User;
import com.dogebook.repositories.FriendRequestRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendResource {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    ResponseEntity<Void> sendFriendRequest(Principal principal, @RequestParam @NotNull Long userId) {
        User invitedUser = userRepository.findById(userId).orElseThrow();
        User invitingUser = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        userRepository.addFriend(invitedUser.getId(), invitingUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    ResponseEntity<List<User>> getFriends(Principal principal) {
        List<User> friends = userRepository.getFriends(UserContext.getUser(principal).getId()).stream().distinct().toList();
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{userToUnfriendId}")
    ResponseEntity<List<User>> unfriend(Principal principal, @PathVariable("userToUnfriendId") Long userToUnfriendId) {
        userRepository.unfriend(UserContext.getUser(principal).getId(), userToUnfriendId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requests-sent")
    ResponseEntity<List<User>> getSentRequests(Principal principal) {
        List<User> friends = userRepository.getSentFriendRequests(UserContext.getUser(principal).getId());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests-received")
    ResponseEntity<List<User>> getReceivedRequests(Principal principal) {
        List<User> friends = userRepository.getReceivedFriendRequests(UserContext.getUser(principal).getId());
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/{invitingUserId}/accept")
    ResponseEntity<Void> acceptFriendRequest(Principal principal, @PathVariable("invitingUserId") Long invitingUserId) {
        userRepository.acceptFriendRequest(UserContext.getUser(principal).getId(), invitingUserId);
        return ResponseEntity.noContent().build();
    }
}