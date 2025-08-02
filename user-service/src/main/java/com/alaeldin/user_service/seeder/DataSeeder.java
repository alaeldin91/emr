package com.alaeldin.user_service.seeder;

import com.alaeldin.exception.not_found.ResourceNotFound;
import com.alaeldin.user_service.constants.RoleName;
import com.alaeldin.user_service.entity.Role;
import com.alaeldin.user_service.entity.User;
import com.alaeldin.user_service.repository.RoleRepository;
import com.alaeldin.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class DataSeeder
{
    @Bean
    public CommandLineRunner seedDataBase(UserRepository userRepository
            , RoleRepository roleRepository)
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
