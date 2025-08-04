package com.alaeldin.user_service.application.serviceImpl;

import com.alaeldin.exception.data_already_exist.DataAlreadyExist;
import com.alaeldin.exception.not_found.ResourceNotFound;
import com.alaeldin.user_service.application.dto.LoginRequest;
import com.alaeldin.user_service.application.dto.UserDto;
import com.alaeldin.user_service.application.dto.UserDtoResponse;
import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.domain.model.User;
import com.alaeldin.user_service.application.mapper.UserMapping;
import com.alaeldin.user_service.domain.repository.RoleRepository;
import com.alaeldin.user_service.infrastructure.repository.UserJpaRepository;
import com.alaeldin.user_service.infrastructure.security.JwtService;
import com.alaeldin.user_service.application.service.LoginService;
import com.alaeldin.user_service.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl  implements UserService , LoginService {

    private final UserJpaRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Override
    public UserDto findByUserName(String username) {
       Optional <User> user = userRepository.findByUserName(username);
       if (user.isEmpty())
       {
           throw new IllegalArgumentException("User with username " + username + " not found.");
       }
         User foundUser = user.get();

       return UserMapping.userDto(foundUser);
    }

    @Override
    public UserDto findByEmail(String email) {

        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isEmpty()) {

            throw new IllegalArgumentException("User with email " + email + " not found.");
        }
        User foundUser = user.get();

        return UserMapping.userDto(foundUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        Long id = userDto.getId();
        User existingUser = userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFound("User", "userId", id.intValue()));
        User updatedUser = userRepository.save(existingUser);

        return UserMapping.userDto(updatedUser);
    }

    @Override
    public UserDto findByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User", "userId", userId.intValue()));

        return UserMapping.userDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFound("User", "userId", userId.intValue());
        }
        userRepository.deleteById(userId);
    }

    @Override
    public boolean existsByUsername(String username) {

        return userRepository.existsByUserName(username);
    }

    @Override
    public boolean existsByEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto registerUser(UserDto userDto) {

        if (existsByEmail(userDto.getEmail())) {
            throw new DataAlreadyExist("Email", userDto.getEmail());
        }

        User user = UserMapping.userEntity(userDto);
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            user.setUserName(user.getEmail().split("@")[0]);
        }

        Set<Role> managedRoles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role dbRole = roleRepository.findByRoleName(role.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role.getRoleName()));
            managedRoles.add(dbRole);
        }
        user.setRoles(managedRoles);

        User savedUser = userRepository.save(user);
        return UserMapping.userDto(savedUser);
    }


    @Override
    public Page<UserDto> getAllUsers(int page, int size) {

        Pageable userPageable = Pageable.ofSize(size).withPage(page);
        Page<User> users = userRepository.findAll(userPageable);

        return users.map(UserMapping::userDto);
    }

    @Override
    public UserDtoResponse login(LoginRequest loginRequest) throws Exception {
        // Authenticate user
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid email or password", e);
        }

        // Retrieve user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFound("User", "UserId", (int)user.getId());
        }

        // Get user role and generate token
        Role userRole = user.getRoles().stream()
                .findFirst()
                .orElseThrow(() -> new Exception("User has no roles assigned"));

        String roleName = userRole.getRoleName().name();
        String token = jwtService.generateToken(user, userRole);

        return UserDtoResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .token(token)
                .build();
    }
}
