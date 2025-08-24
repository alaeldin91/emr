package com.alaeldin.user_service.application.serviceImpl;

import com.alaeldin.user_service.application.dto.LoginRequest;
import com.alaeldin.user_service.application.dto.UserDto;
import com.alaeldin.user_service.application.dto.UserDtoResponse;
import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.domain.model.User;
import com.alaeldin.user_service.application.mapper.UserMapping;
import com.alaeldin.user_service.application.serviceImpl.UserServiceImpl;
import com.alaeldin.user_service.infrastructure.repository.UserJpaRepository;
import com.alaeldin.user_service.infrastructure.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
class UserServiceImplTest {

    private UserDto userDto;
    private User user;

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserMapping userMapping;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        Role role = new Role();
        role.setId(1L);
        role.setRoleName(RoleName.USER);
        role.setDescription("User role");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Ahamed");
        userDto.setLastName("Ali");
        userDto.setUserName("Ahamed");
        userDto.setPhoneNumber("0588530119");
        userDto.setPassword("0588530119");
        userDto.setEmail("ahamed91@gmail.com");
        userDto.setRoles(Set.of(new Role(RoleName.ADMIN)));
        userDto.setActive(true);

        user = new User();
        user.setId(1L);
        user.setFirstName("Ahamed");
        user.setLastName("Ali");
        user.setUserName("Ahamed");
        user.setEmail("ahamed91@gmail.com"); // Added missing email field
        user.setPhoneNumber("0588530119");
        user.setPassword("0588530119");
        user.setRoles(Set.of(role));
        user.setActive(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void registerUser() {
        try (MockedStatic<UserMapping> mockedMapping = Mockito.mockStatic(UserMapping.class)) {
            mockedMapping.when(()->UserMapping.userEntity(any(UserDto.class))).thenReturn(user);
            when(userRepository.save(any(User.class))).thenReturn(user);
            mockedMapping.when(()->UserMapping.userDto(any(User.class))).thenReturn(userDto);

            UserDto result = userService.registerUser(userDto);

            assertNotNull(result);
            assertEquals(userDto.getUserName(), result.getUserName());
        }
    }

    @Test
    void testLogin() throws Exception {

        String email = "ahamed91@gmail.com";
        String password = "0588530119";
        String token = "mocked-jwt-token";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Mock AuthenticationManager
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
        Field authField = UserServiceImpl.class.getDeclaredField("authenticationManager");
        authField.setAccessible(true);
        authField.set(userService, authenticationManager);

        // Fix: authenticate() returns Authentication, not void
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authToken);

        // Mock UserRepository
        when(userRepository.findByEmail(email)).thenReturn(user);

        Role role = user.getRoles().iterator().next();

        // Mock JwtService
        JwtService jwtService = Mockito.mock(JwtService.class);
        Field jwtField = UserServiceImpl.class.getDeclaredField("jwtService");
        jwtField.setAccessible(true);
        jwtField.set(userService, jwtService);
        when(jwtService.generateToken(user, role)).thenReturn(token);

        // Act
        UserDtoResponse userDtoResponse = userService.login(loginRequest);

        // Assertions
        assertNotNull(userDtoResponse);
        assertEquals(user.getEmail(), userDtoResponse.getEmail());
        assertEquals(user.getId(), userDtoResponse.getId());
        assertEquals(user.getFirstName(), userDtoResponse.getFirstName());
        assertEquals(user.getLastName(), userDtoResponse.getLastName());
        assertEquals(user.getPhoneNumber(), userDtoResponse.getPhoneNumber());
        assertEquals(token, userDtoResponse.getToken());
    }


    @Test
    void testUpdateUser() {
        try (MockedStatic<UserMapping> mockedMapping = Mockito.mockStatic(UserMapping.class)) {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            mockedMapping.when(() -> UserMapping.userDto(any(User.class))).thenReturn(userDto);

            UserDto result = userService.updateUser(userDto);

            assertNotNull(result);
            assertEquals(userDto.getUserName(), result.getUserName());
        }
    }

    @Test
    void testFindByUserName() {
        // Mock static method properly
        try (MockedStatic<UserMapping> mockedMapping = Mockito.mockStatic(UserMapping.class)) {
            when(userRepository.findByUserName("Ahamed")).thenReturn(Optional.of(user));
            mockedMapping.when(() -> UserMapping.userDto(any(User.class))).thenReturn(userDto);

            UserDto result = userService.findByUserName("Ahamed");

            assertNotNull(result);
            assertEquals("Ahamed", result.getUserName()); // Use actual expected value
            assertEquals(userDto.getFirstName(), result.getFirstName());
            assertEquals(userDto.getLastName(), result.getLastName());
        }
    }

    @Test
    void TestFindByEmail()
    {
        try(MockedStatic<UserMapping> mockedMapping = Mockito.mockStatic(UserMapping.class))
        {
            // Fix: Mock repository to return User object (not Optional)
            when(userRepository.findByEmail("ahamed91@gmail.com")).thenReturn(user);
            mockedMapping.when(() -> UserMapping.userDto(any(User.class))).thenReturn(userDto);

            UserDto result = userService.findByEmail("ahamed91@gmail.com");

            assertNotNull(result);
            assertEquals("ahamed91@gmail.com", result.getEmail());
            assertEquals(userDto.getFirstName(), result.getFirstName());
            assertEquals(userDto.getLastName(), result.getLastName());
        }
    }

    @Test
    void TestFindByUserId() {

        try (MockedStatic<UserMapping> mockedMapping = Mockito.mockStatic(UserMapping.class)) {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            mockedMapping.when(() -> UserMapping.userDto(any(User.class))).thenReturn(userDto);

            UserDto result = userService.findByUserId(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Ahamed", result.getFirstName());
            assertEquals("Ali", result.getLastName());
            assertEquals("ahamed91@gmail.com", result.getEmail());
            assertEquals("Ahamed", result.getUserName());
        }
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {

        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(Exception.class, () -> userService.deleteUser(userId));

        verify(userRepository).existsById(userId);
        verify(userRepository, Mockito.never()).deleteById(userId);
    }

    @Test
    void testExistsByUsername_UserExists() {
        String username = "Ahamed";
        when(userRepository.existsByUserName(username)).thenReturn(true);

        boolean exists = userService.existsByUsername(username);

        assertTrue(exists);
        verify(userRepository).existsByUserName(username);
    }

    @Test
    void testExistsByUsername_UserDoesNotExist() {
        String username = "NonExistentUser";
        when(userRepository.existsByUserName(username)).thenReturn(false);

        boolean exists = userService.existsByUsername(username);

        assertFalse(exists);
        verify(userRepository).existsByUserName(username);
    }

    @Test
    void testExistByEmail_EmailExists()
    {
        String email = "ahamed91@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean exists = userService.existsByEmail(email);
        assertTrue(exists);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void testExistByEmail_EmailDoesNotExist()
    {
        String email  = "nonexistent@gmail.com"; // Use different email
        when(userRepository.existsByEmail(email))
                .thenReturn(false); // Fixed: should return false
        boolean exists = userService.existsByEmail(email);
        assertFalse(exists);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void TestGetAllUsers()
    {
        try (MockedStatic<UserMapping> mockedMapping = Mockito.mockStatic(UserMapping.class)) {
            // Create test data
            List<User> users = Arrays.asList(user);
            Pageable pageable = PageRequest.of(0, 10);
            Page<User> userPage = new PageImpl<>(users, pageable, users.size());

            // Mock repository and mapping
            when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
            mockedMapping.when(() -> UserMapping.userDto(any(User.class))).thenReturn(userDto);

            // Call service method with correct parameters
            Page<UserDto> result = userService.getAllUsers(0, 10);

            // Assertions
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getContent().size());
            assertEquals(userDto.getUserName(), result.getContent().get(0).getUserName());

            // Verify interactions
            verify(userRepository).findAll(any(Pageable.class));
        }
    }
}
