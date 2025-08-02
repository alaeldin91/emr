package com.alaeldin.user_service.repository;

import com.alaeldin.user_service.constants.RoleName;
import com.alaeldin.user_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long>
{
    Optional<Role> findByRoleName(RoleName roleName);
}
