package com.frank.messaging.types;

import java.util.Date;

import com.frank.messaging.enumeration.MessageType;
import com.frank.messaging.response.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageNotification {
    private UserResponse sender;
    private Integer messageId;
    private String messagePresignedUrl;
    private MessageType messageType;
    private Date sendTime;

}
