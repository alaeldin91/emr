package com.alaeldin.user_service.dto;

import com.alaeldin.user_service.constants.RoleName;
import com.alaeldin.user_service.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoResponse
{
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String phoneNumber;
    private Role roleName;
    private String token;
}
