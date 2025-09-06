package com.frank.messaging.dao;

import java.util.Date;

import com.frank.messaging.dto.UserDTO;
import org.apache.ibatis.annotations.Delete;
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

    @Delete("DELETE FROM user")
    void deleteAll();

    @Update("UPDATE user SET login_token = #{loginToken}, last_login_time = #{lastLoginTime} WHERE id = #{id}")
    void login(String loginToken, Date lastLoginTime, Integer id);

    @Select("SELECT * FROM user WHERE login_token = #{loginToken}")
    UserDTO selectByLoginToken(String loginToken);

    @Update("UPDATE user SET login_token = NULL, last_login_time = NULL WHERE id = #{id}")
    void logout(Integer id);
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
