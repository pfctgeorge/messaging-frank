package com.frank.messaging.dao;

import com.frank.messaging.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO { // DAO = data access object

    @Insert("INSERT INTO user (username, nickname, email, address, gender, password, is_valid, register_time) " +
            "VALUES (#{username}, #{nickname}, #{email}, #{address}, #{gender}, #{password}, #{isValid}, #{registerTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id") // usedDTO.setId(1111);
    void insert(UserDTO userDTO);

    @Select("SELECT * FROM user WHERE username = #{username}")
    UserDTO selectByUsername(String username); // new UserDTO(); userDTO.setId(...); userDTO.setusername(..);

    @Update("UPDATE user SET is_valid = 1 WHERE id = #{id}")
    void updateToValid(int id);
}


//public class UserDAOImpl implements UserDAO {
//
//    @Override
//    public void insert(UserDTO userDTO) {
//        // 1. open a connection to MySQL server
//        // 2. generate a real query by replacing the placeholders
//        // 3. send the real query to database
//
//    }
//
//    @Override
//    public UserDTO selectByUsername(String username) {
//        // 1. open a connection to MySQL server
//        // 2. generate a real query by replacing the placeholders
//        // 3. send the real query to database
//        // 4. parse the result from database
//        // 5. generate a UserDTO with parsed results and return
//    }
//
//    @Override
//    public void updateToValid(int id) {
//
//    }
//}
