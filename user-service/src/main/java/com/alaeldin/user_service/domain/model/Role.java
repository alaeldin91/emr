package com.alaeldin.user_service.domain.model;

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
      public Role()
      {}
      public Role(RoleName roleName)
      {
          this.roleName = roleName;
      }
}
