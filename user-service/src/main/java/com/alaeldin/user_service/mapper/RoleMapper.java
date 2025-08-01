package com.alaeldin.user_service.mapper;

import com.alaeldin.user_service.dto.RoleDto;
import com.alaeldin.user_service.entity.Role;
import jakarta.persistence.Table;

public class RoleMapper
{
    // This class will contain methods to map between Role entities and DTOs
    // For example, it could convert a Role entity to a RoleDTO and vice versa.

    // Example method signature:
     public static RoleDto toDto(Role role)
     {
          RoleDto roleDto = new RoleDto();
          roleDto.setId(role.getId());
          roleDto.setRoleName(role.getRoleName());
          roleDto.setDescription(role.getDescription());

          return roleDto;
     }


     public static Role toEntity(RoleDto roleDTO)
     {
            Role role = new Role();
            role.setId(roleDTO.getId());
            role.setRoleName(roleDTO.getRoleName());
            role.setDescription(roleDTO.getDescription());

            return role;
     }
}
