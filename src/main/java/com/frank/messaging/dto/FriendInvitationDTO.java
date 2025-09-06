package com.frank.messaging.dto;

import java.util.Date;

import com.frank.messaging.enumeration.FriendInvitationStatus;
import lombok.Data;

@Data
public class FriendInvitationDTO {
    private int id;
    private int senderUserId;
    private int receiverUserId;
    private String message;
    private FriendInvitationStatus status;
    private Date sendTime;
}
