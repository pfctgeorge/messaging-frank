package com.frank.messaging.controller;

import java.util.List;

import com.frank.messaging.dto.FriendInvitationDTO;
import com.frank.messaging.dto.UserDTO;
import com.frank.messaging.request.SendFriendInvitationRequest;
import com.frank.messaging.response.FriendInvitationResponse;
import com.frank.messaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired private UserService userService;
    @PostMapping("/invite")
    public void sendFriendInvitation(@CookieValue("login_token") String loginToken,
                                     @RequestBody SendFriendInvitationRequest sendFriendInvitationRequest) {
    }

    @GetMapping("/pendingInvitations")
    public List<FriendInvitationResponse> getPendingFriendInvitations(@CookieValue("login_token") String loginToken,
                                                                      @RequestParam(defaultValue = "1") int page) {
        // start = (page-1) * 10;
        // select * from friend_invitation where receiver_user_id = ? and status = 'PENDING' limit 10, 10;
        return null;
    }

    @PostMapping("/accept")
    public void acceptFriendInvitation(@CookieValue("login_token") String loginToken,
                                       @RequestParam int friendInvitationId) {

    }

    @PostMapping("/reject")
    public void rejectFriendInvitation(@CookieValue("login_token") String loginToken,
                                       @RequestParam int friendInvitationId) {

    }

    @GetMapping("/friends")
    public List<FriendInvitationDTO> getFriends(@CookieValue("login_token") String loginToken,
                                                @RequestParam(defaultValue = "1") int page) {
        // select * from friend_invitation where (sender_user_id = ? or receiver_user_id = ?) and status = 'ACCEPTED' limit 10, 10;
        return null;
    }
}
