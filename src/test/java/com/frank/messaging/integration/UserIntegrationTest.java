package com.frank.messaging.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.frank.messaging.dao.UserDAO;
import com.frank.messaging.dao.UserValidationCodeDAO;
import com.frank.messaging.dto.UserDTO;
import com.frank.messaging.dto.UserValidationCodeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired private MockMvc mockMvc; // HTTP client to your project
    @Autowired private UserDAO userDAO;
    @Autowired private UserValidationCodeDAO userValidationCodeDAO;

    @BeforeEach
    void deleteOldData() {
        this.userDAO.deleteAll();
        this.userValidationCodeDAO.deleteAll();

    }
    // test#{target}_#{scenario}_#{expectation}
    @Test
    void testRegister_passwordIsNotSameWithRepeatPassword_returnsBadRequest() throws Exception {
        String content = """
                {
                    "username": "George23232232323232323232",
                    "password": "123123132dsf",
                    "repeatPassword": "123123132dsfasfa",
                    "nickname": "ssssssss",
                    "gender": "FEMALE",
                    "email": "sdfsdsfdsfdsfsf3@gmail.com",
                    "address": "xcvcxvxcvxcxv"
                }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(content))
                .andExpect(status().isBadRequest()) // status code == 400
                .andExpect(content().string("Passwords are different."));

    }

    @Test
    void testRegister_validRegistrationInformation_returnsOK() throws Exception {
        String content = """
                {
                    "username": "George2323223232323232233232",
                    "password": "123123132dsfasfa",
                    "repeatPassword": "123123132dsfasfa",
                    "nickname": "ssssssss",
                    "gender": "FEMALE",
                    "email": "sdfsdsfdsfds2fsf23@gmail.com",
                    "address": "xcvcxvxcvxcxv"
                }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(content))
                .andExpect(status().isOk());

        UserDTO userDTO = this.userDAO.selectByUsername("George2323223232323232233232");
        assertNotNull(userDTO);
        assertEquals("123123132dsfasfa", userDTO.getPassword());
        assertEquals("ssssssss", userDTO.getNickname());
        assertNotNull(userDTO.getRegisterTime());

        UserValidationCodeDTO userValidationCodeDTO = this.userValidationCodeDAO.selectByUserId(userDTO.getId());
        assertNotNull(userValidationCodeDTO);
        assertEquals(userDTO.getId(), userValidationCodeDTO.getUserId());
        assertEquals(6, userValidationCodeDTO.getValidationCode().length());
    }

    @Test
    void testActivate_validActivationInformation_returnsOK() throws Exception {
        String content = """
                {
                    "username": "George2323223232323232233232",
                    "password": "123123132dsfasfa",
                    "repeatPassword": "123123132dsfasfa",
                    "nickname": "ssssssss",
                    "gender": "FEMALE",
                    "email": "sdfsdsfdsfds2fsf23@gmail.com",
                    "address": "xcvcxvxcvxcxv"
                }
                """;
        this.mockMvc.perform(post("/users/register")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(content));
        UserDTO userDTO = this.userDAO.selectByUsername("George2323223232323232233232");
        UserValidationCodeDTO userValidationCodeDTO = this.userValidationCodeDAO.selectByUserId(userDTO.getId());

        content = String.format("""
                {
                    "username": "George2323223232323232233232",
                    "validationCode": "%s"
                }
                """, userValidationCodeDTO.getValidationCode());
        this.mockMvc.perform(post("/users/activate")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(content))
                .andExpect(status().isOk());

        userDTO = this.userDAO.selectByUsername("George2323223232323232233232");
        assertTrue(userDTO.getIsValid());

        userValidationCodeDTO = this.userValidationCodeDAO.selectByUserId(userDTO.getId());
        assertNull(userValidationCodeDTO);
    }

    @Test
    void testActivate_nonExistingUser_returns400() throws Exception {
        var content = """
                {
                    "username": "randomuser",
                    "validationCode": "123456"
                }
                """;
        this.mockMvc.perform(post("/users/activate")
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User does not exist"));

    }
}
