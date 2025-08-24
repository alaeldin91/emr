package com.alaeldin.user_service.domain;

import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest
{
    private User user;

    @BeforeEach
    void setUp()
    {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setRoleName(RoleName.ADMIN);
        role1.setDescription("Administrator role with " +
                "full access");

       Role role2 = new Role();
       role2.setId(2L);
       role2.setRoleName(RoleName.DOCTOR);
       role2.setDescription("Doctor role with access to patient records and medical " +
                "data");
       user = new User();
       user.setId(1L);
       user.setFirstName("Alaleldin");
       user.setLastName("Musa");
       user.setUserName("alaeldinmusa91@gmail.com");
       user.setEmail("alaeldinmusa91@gmail.com");
       user.setPassword("password123");
       user.setPhoneNumber("01000000000");
       user.setActive(true);
       user.setRoles(Set.of(role1, role2));

    }

    @Test
    void testUserDetailsInterfaceMethods()
    {
        assertEquals("password123", user.getPassword());
        assertEquals("alaeldinmusa91@gmail.com", user.getUsername()); // Test the actual userName field
        assertEquals("alaeldinmusa91@gmail.com",user.getEmail());
        assertEquals("01000000000", user.getPhoneNumber());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isActive());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());

    }

    @Test
    void testGrantedAuthorities()
    {
        var authorities = user.getAuthorities();
        assertEquals(2, authorities.size());
        //assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals(RoleName.ADMIN)));
      boolean isAdmin = authorities.stream().map(GrantedAuthority::getAuthority)
              .anyMatch(auth-> auth.equals("ROLE_" + RoleName.ADMIN.toString()));
        boolean isDoctor = authorities.stream().map(GrantedAuthority::getAuthority)
                .anyMatch(auth-> auth.equals("ROLE_" + RoleName.DOCTOR.toString()));
        assertTrue(isAdmin);
        assertTrue(isDoctor);
    }

    @Test
    void testBasicFields()
    {
        assertEquals(1L,user.getId());
        assertEquals("Alaleldin", user.getFirstName());
        assertEquals("Musa", user.getLastName());
        assertEquals("password123",user.getPassword());
        assertEquals("alaeldinmusa91@gmail.com",user.getEmail());
        assertEquals("01000000000", user.getPhoneNumber());
        assertEquals("alaeldinmusa91@gmail.com",user.getUsername());


    }
}
