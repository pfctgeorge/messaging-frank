package com.frank.messaging.request;

import lombok.Data;

@Data
public class SendFriendInvitationRequest {
    private int receiverUserId;
    private String message;
}
