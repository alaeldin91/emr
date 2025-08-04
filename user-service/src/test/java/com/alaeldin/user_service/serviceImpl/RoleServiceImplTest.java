package com.alaeldin.user_service.serviceImpl;

import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.application.dto.RoleDto;
import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.application.mapper.RoleMapper;
import com.alaeldin.user_service.application.serviceImpl.RoleServiceImpl;
import com.alaeldin.user_service.domain.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
        try (MockedStatic<RoleMapper> mockedMapper
                     = mockStatic(RoleMapper.class)) {
            mockedMapper.when(() -> RoleMapper.toEntity(any(RoleDto.class))).thenReturn(role);
            mockedMapper.when(() -> RoleMapper.toDto(any(Role.class))).thenReturn(roleDto);
            when(roleRepository.save(any(Role.class))).thenReturn(role);

            RoleDto result = roleService.createRole(roleDto);

            assertNotNull(result);
            assertEquals(roleDto.getRoleName(), result.getRoleName());
            assertEquals(roleDto.getDescription(), result.getDescription());
        }
    }

    @Test
    void testUpdateRole() {
        try (MockedStatic<RoleMapper> mockedMapper = mockStatic(RoleMapper.class)) {
            mockedMapper.when(() -> RoleMapper.toEntity(any(RoleDto.class))).thenReturn(role);
            mockedMapper.when(() -> RoleMapper.toDto(any(Role.class))).thenReturn(roleDto);
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role)); // Add this mock
            when(roleRepository.save(any(Role.class))).thenReturn(role);

            RoleDto result = roleService.updateRole(roleDto);

            assertNotNull(result);
            assertEquals(roleDto.getRoleName(), result.getRoleName());
            assertEquals(roleDto.getDescription(), result.getDescription());
        }
    }

    @Test
    void testFindDByRoleName() {
        try (MockedStatic<RoleMapper> mockedMapper = mockStatic(RoleMapper.class)) {
            mockedMapper.when(() -> RoleMapper.toDto(any(Role.class))).thenReturn(roleDto);
            when(roleRepository.findByRoleName(RoleName.ADMIN)).thenReturn(Optional.of(role));

            RoleDto result = roleService.findDByRoleName(RoleName.ADMIN);

            assertNotNull(result);
            assertEquals(roleDto.getRoleName(), result.getRoleName());
        }
    }

    @Test
    void testFindByRoleId() {
        try (MockedStatic<RoleMapper> mockedMapper = mockStatic(RoleMapper.class)) {
            mockedMapper.when(() -> RoleMapper.toDto(any(Role.class))).thenReturn(roleDto);
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

            RoleDto result = roleService.findByRoleId(1L);

            assertNotNull(result);
            assertEquals(roleDto.getId(), result.getId());
        }
    }

    @Test
    void testGetAllRoles() {
        try (MockedStatic<RoleMapper> mockedMapper = mockStatic(RoleMapper.class)) {
            mockedMapper.when(() -> RoleMapper.toDto(any(Role.class))).thenReturn(roleDto);
            when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

            List<RoleDto> result = roleService.getAllRoles();

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(1, result.size());
        }
    }

    @Test
    void testDeleteRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).delete(any(Role.class));

        assertDoesNotThrow(() -> roleService.deleteRole(1L));

        verify(roleRepository, times(1)).findById(1L);
        verify(roleRepository, times(1)).delete(role);
    }
}
