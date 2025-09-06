package com.frank.messaging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.frank.messaging.dao.UserDAO;
import com.frank.messaging.dao.UserValidationCodeDAO;
import com.frank.messaging.dto.UserDTO;
import com.frank.messaging.dto.UserValidationCodeDTO;
import com.frank.messaging.enumeration.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserDAO userDAO;
    @Mock UserValidationCodeDAO userValidationCodeDAO;
    @Captor ArgumentCaptor<UserDTO> userDTOArgumentCaptor;
    @Captor ArgumentCaptor<UserValidationCodeDTO> userValidationCodeDTOArgumentCaptor;

    @InjectMocks UserService userService;

    @Test
    void testRegister_twoPasswordsNotSame_throwsException() {

        Exception exception = assertThrows(Exception.class,
                                           () ->
                                                   this.userService.register("username",
                                                                             "password",
                                                                             "differentPassword",
                                                                             "email",
                                                                             "nickname",
                                                                             "address",
                                                                             Gender.MALE));
        assertEquals("Passwords are different.", exception.getMessage());

    }

    @Test
    void testRegister_validInput_registrationSucceeded() throws Exception {

        doAnswer(invocation -> {
                    UserDTO userDTO = invocation.getArgument(0);
                    userDTO.setId(1);
                    return null;
                }
        ).when(this.userDAO).insert(any());

        doNothing().when(this.userValidationCodeDAO).insert(any());

        this.userService.register("username",
                                  "password",
                                  "password",
                                  "email@gmail.com",
                                  "nickname",
                                  "address",
                                  Gender.MALE);

        verify(this.userDAO).insert(this.userDTOArgumentCaptor.capture());
        UserDTO userDTO = this.userDTOArgumentCaptor.getValue();
        assertEquals("username", userDTO.getUsername());
        assertEquals("password", userDTO.getPassword());
        assertEquals("email@gmail.com", userDTO.getEmail());
        assertEquals("nickname", userDTO.getNickname());
        assertEquals("address", userDTO.getAddress());
        assertEquals(Gender.MALE, userDTO.getGender());
        assertFalse(userDTO.getIsValid());

        verify(this.userValidationCodeDAO).insert(this.userValidationCodeDTOArgumentCaptor.capture());
        UserValidationCodeDTO userValidationCodeDTO = this.userValidationCodeDTOArgumentCaptor.getValue();
        assertEquals(1, userValidationCodeDTO.getUserId());
    }

    @Test
    void testActivate_userDoesNotExist_throwsException() {
        when(this.userDAO.selectByUsername("username")).thenReturn(null);

        Exception exception = assertThrows(Exception.class,
                                           () -> this.userService.activate("username", "validationCode"));

        assertEquals("User does not exist", exception.getMessage());
    }

    @Test
    void testActivate_wrongValidationCode_throwsException() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        UserValidationCodeDTO userValidationCodeDTO = new UserValidationCodeDTO();
        userValidationCodeDTO.setValidationCode("123456");

        when(this.userDAO.selectByUsername("username")).thenReturn(userDTO);
        when(this.userValidationCodeDAO.selectByUserId(1)).thenReturn(userValidationCodeDTO);

        Exception exception = assertThrows(Exception.class,
                                           () -> this.userService.activate("username", "654321"));

        assertEquals("Wrong user validation code", exception.getMessage());
    }
}
