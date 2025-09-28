package com.frank.messaging.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GroupChatDTO {

    private int id;
    private String name;
    private String description;
    private int creatorUserId;
    private Date createTime;
    private List<Integer> memberUserIds; // 1,2,3
}
