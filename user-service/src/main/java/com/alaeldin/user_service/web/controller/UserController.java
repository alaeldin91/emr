package com.alaeldin.user_service.web.controller;

import com.alaeldin.user_service.application.dto.LoginRequest;
import com.alaeldin.user_service.application.dto.UserDto;
import com.alaeldin.user_service.application.dto.UserDtoResponse;
import com.alaeldin.user_service.application.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Api",description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Operation(summary = "Register a new user", description = "Endpoint to register a new user with the provided details.")
    @PostMapping("/")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        UserDto savedUser = userService.registerUser(userDto);

        return ResponseEntity.ok(savedUser);
    }

    @Operation(summary = "Get all users", description = "Endpoint to retrieve a paginated list of all users.")
    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Page<UserDto> users = userService.getAllUsers(page, size);

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Endpoint to retrieve a user by their unique ID.")
    @GetMapping("/userId")
    public ResponseEntity<UserDto> getUserById(@RequestParam("userId") Long userId) {
        UserDto user = userService.findByUserId(userId);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user by username", description = "Endpoint to retrieve a user by their username.")
    @GetMapping("/username")
    public ResponseEntity<UserDto> getUserByUserName(@RequestParam("username") String username) {
        UserDto user = userService.findByUserName(username);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get user by email", description = "Endpoint to retrieve a user by their email address.")
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) {
        UserDto user = userService.findByEmail(email);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update user", description = "Endpoint to update an existing user's details.")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        UserDto updatedUser = userService.updateUser(userDto);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user", description = "Endpoint to delete a user by their unique ID.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.ok("Successfully deleted user with ID: " + userId);
    }

    @Operation(summary = "Login user", description = "Endpoint to authenticate a user with their Email and password.")
    @PostMapping("/login")
    public ResponseEntity<UserDtoResponse> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {

        UserDtoResponse user = userService.login(loginRequest);

        return ResponseEntity.ok(user);
    }
}
