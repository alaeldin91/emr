package com.alaeldin.user_service.application.dto;

import com.alaeldin.user_service.domain.model.RoleName;
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
