package com.frank.messaging.dto;

import lombok.Data;

@Data
public class GroupChatMemberDTO {
    private int id;
    private int groupChatId; // index groupChatIdIndex select where group_chat_id = xxx;
    private int userId; // index userIdIndex select where user_id = xxx;
}
