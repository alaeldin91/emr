package com.alaeldin.user_service.application.serviceImpl;

import com.alaeldin.exception.not_found.ResourceNotFound;
import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.application.dto.RoleDto;
import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.application.mapper.RoleMapper;
import com.alaeldin.user_service.application.service.RoleService;
import com.alaeldin.user_service.infrastructure.repository.RoleJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl  implements RoleService
{
    @Autowired
    private RoleJpaRepository roleRepository;
    /** * RoleServiceImpl is a service implementation for managing roles in the user service.
     * It implements the RoleService interface and provides methods for creating, updating,
     * finding, retrieving all roles, and deleting roles.
     */
    @Override
    public RoleDto createRole(RoleDto roleDto) {
        if (roleDto.getRoleName() == null) {
            throw new IllegalArgumentException("Role name must not be null.");
        }

        RoleName roleName = roleDto.getRoleName();

        if (roleRepository.findByRoleName(roleName).isPresent()) {
            //throw new ResourceAlreadyExistsException("Role with name " + roleName + " already exists.");
        }

        Role role = RoleMapper.toEntity(roleDto);
        Role savedRole = roleRepository.save(role);

        return RoleMapper.toDto(savedRole);
    }


    /**
     * Updates an existing role.
     *
     * @param roleDto the role data transfer object containing updated role information
     * @return the updated RoleDto
     */
    @Override
    public RoleDto updateRole(RoleDto roleDto) {
        Long roleId = roleDto.getId();
       Role existingRole = roleRepository.findById(roleId)
               .orElseThrow(()->new ResourceNotFound("Role","roleId",roleId.intValue()));

       existingRole.setId(roleDto.getId());
       existingRole.setRoleName(roleDto.getRoleName());
       existingRole.setDescription(roleDto.getDescription());
       Role updatedRole = roleRepository.save(existingRole);

       return RoleMapper.toDto(updatedRole);
    }

    /**
     * Finds a role by its name.
     *
     * @param roleName the name of the role to find
     * @return an Optional containing the RoleDto if found, or empty if not found
     */
    @Override
    public RoleDto findDByRoleName(RoleName roleName) {
        if (roleName == null) {

            throw new IllegalArgumentException("Role name must not be null or empty.");
        }

        Role role = roleRepository.findByRoleName(roleName).orElseThrow();

        return RoleMapper.toDto(role);
    }

    /**
     * Finds a role by its ID.
     *
     * @param roleId the ID of the role to find
     * @return an Optional containing the RoleDto if found, or empty if not found
     */
    @Override
    public RoleDto findByRoleId(Long roleId) {
          if (roleId == null) {
              throw new IllegalArgumentException("Role ID must not be null.");
          }
          Role role = roleRepository.findById(roleId)
                  .orElseThrow(()-> new ResourceNotFound("Role with ID " + roleId
                          + " not found.","roleId",roleId.intValue()));

            return RoleMapper.toDto(role);
    }

    /**
     * Retrieves all roles.
     * @return
     */
    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a role by its ID.
     *
     * @param roleId the ID of the role to delete
     */
    @Override
    public void deleteRole(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID must not be null.");
        }

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFound("Role with ID " + roleId
                        + " not found.", "roleId", roleId.intValue()));

        roleRepository.delete(role);
    }
}
