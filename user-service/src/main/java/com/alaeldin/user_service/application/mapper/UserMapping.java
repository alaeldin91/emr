package com.alaeldin.user_service.application.mapper;

import com.alaeldin.user_service.application.dto.UserDto;
import com.alaeldin.user_service.domain.model.User;

public class UserMapping
{
     public static UserDto userDto(User user)
     {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setUserName(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setRoles(user.getRoles());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setActive(user.isActive());

            return  userDto;
     }

     public static User userEntity(UserDto userDto)
     {
            User user = new User();
         if (userDto.getId() != null) {
             user.setId(userDto.getId());
         }

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setUserName(userDto.getUserName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setRoles(userDto.getRoles());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setActive(userDto.isActive());

           return user;
     }
}
