package com.alaeldin.user_service.infrastructure.repository;

import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.domain.repository.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository  extends JpaRepository<Role,Long>, RoleRepository
{
    @Override
    Optional<Role> findByRoleName(RoleName roleName);
}
