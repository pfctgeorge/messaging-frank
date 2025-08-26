package com.frank.messaging.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().isOk()); // status code == 400

    }
}
