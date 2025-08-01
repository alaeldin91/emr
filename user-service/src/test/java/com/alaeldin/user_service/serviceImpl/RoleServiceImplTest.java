package com.alaeldin.user_service.serviceImpl;

import com.alaeldin.user_service.constants.RoleName;
import com.alaeldin.user_service.dto.RoleDto;
import com.alaeldin.user_service.entity.Role;
import com.alaeldin.user_service.mapper.RoleMapper;
import com.alaeldin.user_service.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleMapper roleMapper;
    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setId(1L);
        role.setRoleName(RoleName.ADMIN);
        role.setDescription("Admin role");
        roleDto = new RoleDto();
        roleDto.setId(1L);
        roleDto.setRoleName(RoleName.ADMIN);
        roleDto.setDescription("Admin role");
    }

    @Test
    void testCreateRole() {
        when(roleMapper.toEntity(any(RoleDto.class))).thenReturn(role);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);
        RoleDto result = roleService.createRole(roleDto);
        assertNotNull(result);
        assertEquals(roleDto.getRoleName(), result.getRoleName());
    }

    @Test
    void testUpdateRole() {
        when(roleMapper.toEntity(any(RoleDto.class))).thenReturn(role);
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);
        RoleDto result = roleService.updateRole(roleDto);
        assertNotNull(result);
        assertEquals(roleDto.getRoleName(), result.getRoleName());
    }

    @Test
    void testFindDByRoleName() {
        when(roleRepository.findByRoleName(RoleName.ADMIN.getValue()))
                .thenReturn(Optional.of(role));
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);
        RoleDto result = roleService.findDByRoleName("ADMIN");
        assertNotNull(result);
        assertEquals(roleDto.getRoleName(), result.getRoleName());
    }


    @Test
    void testFindByRoleId() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);
        RoleDto result = roleService.findByRoleId(1L);
        assertNotNull(result);
        assertEquals(roleDto.getId(), result.getId());
    }

    @Test
    void testGetAllRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);
        List<RoleDto> result = roleService.getAllRoles();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteRole() {
        doNothing().when(roleRepository).deleteById(1L);
        assertDoesNotThrow(() -> roleService.deleteRole(1L));
        verify(roleRepository, times(1)).deleteById(1L);
    }
}
