package com.alaeldin.user_service.entity;

import com.alaeldin.user_service.constants.RoleName;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "roles")
@Entity
@Data
public class Role
{
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
     @Column(name = "role_name", unique = true, nullable = false)
      @Enumerated(EnumType.STRING)
      private RoleName roleName;
      @Column(name = "description" )
      private String description;
}
