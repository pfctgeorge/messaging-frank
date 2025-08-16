package com.frank.messaging.dao;

import com.frank.messaging.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO { // DAO = data access object

    @Insert("INSERT INTO user (username, nickname, email, address, gender, password, is_valid, register_time) " +
            "VALUES (#{username}, #{nickname}, #{email}, #{address}, #{gender}, #{password}, #{isValid}, #{registerTime})")
    void insert(UserDTO userDTO);
}
