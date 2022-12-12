package com.dogebook.controllers;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.Friend;
import com.dogebook.entities.User;
import com.dogebook.repositories.FriendRequestRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    ResponseEntity<Void> sendFriendRequest(Principal principal, @RequestParam @NotNull Long userId) throws URISyntaxException {
        User invitee = userRepository.findById(userId).orElseThrow();
        User invitingUser = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow();
        userRepository.addFriend(invitee.getId(), invitingUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    ResponseEntity<List<Friend>> getFriends(Principal principal) {
        List<Friend> friends = userRepository.findById(UserContext.getUser(principal).getId()).orElseThrow().getFriends();
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/{requestId}/accept")
    ResponseEntity<Void> acceptFriendRequest(@RequestParam("requestId") Long requestId) {
        Friend request = friendRequestRepository.findById(requestId).orElseThrow();
        request.setAccepted(true);
        User invitee = userRepository.findById(request.getInvitingUserId()).orElseThrow();
//        invitee.getFriends().add(userRepository.findById(request.getSendingUserId()).orElseThrow());
        return ResponseEntity.noContent().build();
    }
}