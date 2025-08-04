package com.alaeldin.user_service.infrastructure.seeder;

import com.alaeldin.user_service.domain.model.RoleName;
import com.alaeldin.user_service.domain.model.Role;
import com.alaeldin.user_service.domain.model.User;
import com.alaeldin.user_service.domain.repository.RoleRepository;
import com.alaeldin.user_service.infrastructure.repository.RoleJpaRepository;
import com.alaeldin.user_service.infrastructure.repository.UserJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class DataSeeder
{
    @Bean
    public CommandLineRunner seedDataBase(UserJpaRepository userRepository
            , RoleJpaRepository roleRepository)
    {
         return args ->
         {
             Role roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN)
                        .orElseGet(() -> roleRepository.save(new Role(RoleName.ADMIN)));
                Role roleUser = roleRepository.findByRoleName(RoleName.USER).orElseGet(()
                        -> roleRepository.save(new Role(RoleName.USER)));
                 User user = userRepository.findByEmail("admin@Example.com");
                if (user == null) {
                    user = new User();

                    user.setFirstName("System");
                    user.setEmail("admin@Example.com");
                    user.setLastName("Admin");
                    user.setUserName("admin");
                    user.setPassword("admin123");
                    user.setPhoneNumber("01000000000");
                    user.setActive(true);
                    user.setPassword(new BCryptPasswordEncoder().encode("admin123"));
                    user.setRoles(Set.of(roleAdmin, roleUser));

                    //Save the user to the database
                    userRepository.save(user);
                    System.out.println("Admin user created with email: " + user.getEmail());
                 }
                else
                {
                    System.out.println("Admin user already exists with email: " + user.getEmail());
                }

         };
    }
}
