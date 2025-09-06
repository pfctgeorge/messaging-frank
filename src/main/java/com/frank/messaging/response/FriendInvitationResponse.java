package com.frank.messaging.response;

import java.util.Date;

import com.frank.messaging.enumeration.FriendInvitationStatus;

public class FriendInvitationResponse {
    private int id;
    private UserResponse senderUserId;
    private UserResponse receiverUserId;
    private String message;
    private FriendInvitationStatus status;
    private Date sendTime;
}
