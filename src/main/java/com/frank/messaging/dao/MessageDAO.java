package com.frank.messaging.dao;

import com.frank.messaging.dto.MessageDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageDAO {
    @Insert("INSERT INTO message (sender_user_id, receiver_user_id, group_chat_id, send_time, message_type) " +
            "VALUES (#{senderUserId}, #{receiverUserId}, #{groupChatId}, #{sendTime}, #{messageType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id") // usedDTO.setId(1111);
    void insert(MessageDTO messageDTO);
}
