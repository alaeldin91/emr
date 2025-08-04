package com.alaeldin.user_service.application.dto;

import com.alaeldin.user_service.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
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

    public UserDtoResponse() {
        // Default constructor
    }
}
