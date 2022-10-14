package com.dogebook.controllers;

import com.dogebook.entities.FriendRequest;
import com.dogebook.entities.User;
import com.dogebook.repositories.FriendRequestRepository;
import com.dogebook.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/friends")
public class FriendResource {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @PostMapping
    ResponseEntity<Void> sendFriendRequest(@RequestParam Long userId) throws URISyntaxException {
        FriendRequest request = new FriendRequest();
        request.setSendingUserId(0L);
        Long id = friendRequestRepository.save(request).getId();
        return ResponseEntity.created(new URI("/friends/" + id)).build();
    }

    @PostMapping("/{requestId}/accept")
    ResponseEntity<Void> acceptFriendRequest(@RequestParam("requestId") Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow();
        request.setAccepted(true);
        User invitee = userRepository.findById(request.getInvitedUserId()).orElseThrow();
        invitee.getFriends().add(userRepository.findById(request.getSendingUserId()).orElseThrow());
        return ResponseEntity.noContent().build();
    }
}