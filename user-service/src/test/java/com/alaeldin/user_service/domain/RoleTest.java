package com.alaeldin.user_service.domain;

import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.domain.model.RoleName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest
{
    @Test
    void testDefaultConstructorAndSetter()
    {
        Role role = new Role();

        role.setId(1L);
        role.setRoleName(RoleName.ADMIN);
        role.setDescription("Admin Role Description ");

        //Assertions can be added here to verify the values
        assertEquals(1L, role.getId());
        assertEquals(RoleName.ADMIN,role.getRoleName());
        assertEquals("Admin Role Description "
                , role.getDescription());
    }

    @Test
    void testCustomConstructor()
    {
        Role role = new Role(RoleName.ADMIN);
        assertNull(role.getDescription());
        assertEquals(RoleName.ADMIN, role.getRoleName());

    }

    @Test
    void testRoleEnumValues()
    {
        RoleName[] roleNames = RoleName.values();
        assertTrue(roleNames.length > 0);
        assertTrue(RoleName.valueOf("ADMIN").toString().contains("ADMIN"));

    }
}
