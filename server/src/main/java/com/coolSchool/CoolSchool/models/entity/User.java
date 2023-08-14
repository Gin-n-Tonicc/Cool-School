package com.coolSchool.CoolSchool.models.entity;

import com.coolSchool.CoolSchool.enums.Role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String username;
    private String address;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "is_deleted")
    private boolean deleted;
}
