package com.alaeldin.user_service.controller;

import com.alaeldin.user_service.dto.LoginRequest;
import com.alaeldin.user_service.dto.UserDto;
import com.alaeldin.user_service.dto.UserDtoResponse;
import com.alaeldin.user_service.entity.User;
import com.alaeldin.user_service.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = new UserDto();
        when(userService.registerUser(userDto)).thenReturn(userDto);
        ResponseEntity<UserDto> response = userController.registerUser(userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void testUpdateUser()
    {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        when(userService.updateUser(userDto)).thenReturn(userDto);
        ResponseEntity<UserDto> response = userController.updateUser(userId, userDto);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(userDto, response.getBody());

    }

    @Test
    void testDeleteUser()
    {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        ResponseEntity<String> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted user with ID: " + userId,response.getBody());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testGetAllUsers() {
        UserDto user1 = new UserDto();
        UserDto user2 = new UserDto();
        Page<UserDto> page = new PageImpl<>(Arrays.asList(user1, user2));
        when(userService.getAllUsers(0, 2)).thenReturn(page);
        ResponseEntity<Page<UserDto>> response = userController.getAllUsers(0, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    void testGetUserById() {
        UserDto user = new UserDto();
        when(userService.findByUserId(1L)).thenReturn(user);
        ResponseEntity<UserDto> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserByUserName() {
        UserDto user = new UserDto();
        when(userService.findByUserName("testuser")).thenReturn(user);
        ResponseEntity<UserDto> response = userController.getUserByUserName("testuser");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUserByEmail()
    {
        UserDto userDto = new UserDto();
        when(userService.findByEmail("tesruser@gmail.com")).thenReturn(userDto);
        ResponseEntity<UserDto> response = userController.getUserByEmail("tesruser@gmail.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void testLoginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .email("test@example.com")
                .token("jwt-token")
                .build();
        when(userService.login(loginRequest)).thenReturn(userDtoResponse);
        ResponseEntity<UserDtoResponse> response = userController.loginUser(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDtoResponse, response.getBody());
        verify(userService, times(1)).login(loginRequest);
    }
}
