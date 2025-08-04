package com.alaeldin.user_service.domain.repository;

import com.alaeldin.user_service.domain.model.User;

import java.util.Optional;

public interface UserRepository
{
    Optional<User> findByUserName(String username);
    User findByEmail(String email);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
