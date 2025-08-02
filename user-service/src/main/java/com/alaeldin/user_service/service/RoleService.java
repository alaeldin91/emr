package com.alaeldin.user_service.service;

import com.alaeldin.user_service.constants.RoleName;
import com.alaeldin.user_service.dto.RoleDto;
import com.alaeldin.user_service.entity.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * RoleService interface provides methods for managing roles in the user service.
 * It includes methods for creating, updating, finding, retrieving all roles, and deleting roles.
 */
public interface RoleService
{
    // Creates a new role
    RoleDto createRole(RoleDto roleDto);
    // Updates an existing role
    RoleDto updateRole(RoleDto roleDto);
    // Finds a role by its name
    RoleDto findDByRoleName(RoleName roleName);
    // Finds a role by its ID
    RoleDto findByRoleId(Long roleId);
    // Retrieves all roles
    List<RoleDto> getAllRoles();
    // Deletes a role by its ID
    void deleteRole(Long roleId);

}
