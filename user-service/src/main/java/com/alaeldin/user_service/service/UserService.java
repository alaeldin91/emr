package com.alaeldin.user_service.service;

import com.alaeldin.user_service.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService
{
    UserDto findByUserName(String username);
    UserDto findByEmail(String email);
    UserDto updateUser(UserDto userDto);
    UserDto findByUserId(Long userId);
    void deleteUser(Long userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    UserDto registerUser(UserDto userDto);
    Page<UserDto> getAllUsers(int page, int size);
}
