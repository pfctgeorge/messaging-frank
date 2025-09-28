package com.frank.messaging.dto;

import java.util.Date;

import com.frank.messaging.enumeration.MessageType;
import lombok.Data;

@Data
public class MessageDTO {
    private int id;
    private int senderUserId;
    private Integer receiverUserId;
    private Integer groupChatId;
    private Date sendTime;
    private MessageType messageType;
}
