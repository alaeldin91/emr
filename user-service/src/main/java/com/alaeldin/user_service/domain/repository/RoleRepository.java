package com.alaeldin.user_service.domain.repository;

import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository
{
    Optional<Role> findByRoleName(RoleName roleName);
}
