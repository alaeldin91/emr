package com.alaeldin.user_service.controller;

import com.alaeldin.user_service.constants.RoleName;
import com.alaeldin.user_service.dto.RoleDto;
import com.alaeldin.user_service.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {
    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRoles() {
        RoleDto role1 = new RoleDto();
        RoleDto role2 = new RoleDto();
        when(roleService.getAllRoles()).thenReturn(Arrays.asList(role1, role2));
        ResponseEntity<List<RoleDto>> response = roleController.getAllRoles();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetRoleById() {
        RoleDto role = new RoleDto();
        when(roleService.findByRoleId(1L)).thenReturn(role);
        ResponseEntity<RoleDto> response = roleController.getRoleById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void testGetRoleByName() {
        RoleDto role = new RoleDto();
        when(roleService.findDByRoleName(RoleName.ADMIN)).thenReturn(role);
        ResponseEntity<RoleDto> response = roleController.getRoleByName(RoleName.ADMIN.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void testCreateRole() {
        RoleDto role = new RoleDto();
        when(roleService.createRole(role)).thenReturn(role);
        ResponseEntity<RoleDto> response = roleController.createRole(role);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void testUpdateRole() {
        RoleDto role = new RoleDto();
        role.setId(1L);
        when(roleService.updateRole(role)).thenReturn(role);
        ResponseEntity<RoleDto> response = roleController.updateRole(1L, role);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role, response.getBody());
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleService).deleteRole(1L);
        ResponseEntity<Void> response = roleController.deleteRole(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}

