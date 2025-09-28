package com.frank.messaging.request;

import com.frank.messaging.enumeration.MessageType;
import lombok.Data;

@Data
public class SendMessageRequest {

    private Integer receiverUserId;
    private Integer groupChatId;
    private MessageType messageType;
}
