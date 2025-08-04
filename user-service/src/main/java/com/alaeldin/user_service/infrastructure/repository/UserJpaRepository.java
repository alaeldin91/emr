package com.alaeldin.user_service.infrastructure.repository;

import com.alaeldin.user_service.domain.model.User;
import com.alaeldin.user_service.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long>, UserRepository
{
    @Override
    Optional<User> findByUserName(String username);

    @Override
    User findByEmail(String email);

    @Override
    boolean existsByUserName(String username);

    @Override
    boolean existsByEmail(String email);
}
