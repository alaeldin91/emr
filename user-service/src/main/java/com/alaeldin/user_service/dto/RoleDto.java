package com.alaeldin.user_service.dto;

import com.alaeldin.user_service.constants.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RoleDto
{

    private long id;
    @NotNull(message = "Role name cannot be null")
    private RoleName roleName;
    private String description;
}
