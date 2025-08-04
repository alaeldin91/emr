package com.alaeldin.user_service.web.controller;

import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.application.dto.RoleDto;
import com.alaeldin.user_service.application.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Role API", description = "Endpoints for managing roles")
@RestController
@RequestMapping("/api/roles")
public class RoleController
{
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Get all roles", description = "Retrieve a list of all roles.")
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {

        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @Operation(summary = "Get role by ID", description = "Retrieve a role by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(
            @Parameter(description = "ID of the role to retrieve", example = "1")
            @PathVariable Long id) {
        RoleDto role = roleService.findByRoleId(id);

        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Get role by name", description = "Retrieve a role by its name.")
    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleDto> getRoleByName(
            @Parameter(description = "Name of the role to retrieve", example = "ADMIN")
            @PathVariable String roleName) {

        // Convert String to Enum safely
        RoleName roleEnum;
        try {
            roleEnum = RoleName.valueOf(roleName.toUpperCase()); // Convert to enum constant
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Invalid role name
        }

        RoleDto role = roleService.findDByRoleName(roleEnum);

        return ResponseEntity.ok(role);
    }


    @Operation(summary = "Create a new role", description = "Create a new role with the provided details.")
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto createdRole = roleService.createRole(roleDto);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a role", description = "Update an existing role by its ID.")
    @PutMapping("/{id}")
    public ResponseEntity<RoleDto> updateRole(
            @Parameter(description = "ID of the role to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RoleDto roleDto) {
        roleDto.setId(id);
        RoleDto updatedRole = roleService.updateRole(roleDto);
        return ResponseEntity.ok(updatedRole);
    }

    @Operation(summary = "Delete a role", description = "Delete a role by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID of the role to delete", example = "1")
            @PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
