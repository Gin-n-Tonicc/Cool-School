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
    @NotNull(message = "Enter the name of the user")
    @Size(min = 4)
    private String name;
    @Size(min = 5)
    @NotNull(message = "Enter the last name of the user")
    private String lastname;
    @Email
    @NotNull(message = "Enter the email of the user")
    private String email;
    @NotNull(message = "Enter a username")
    private String username;
    private String address;
    @NotNull(message = "Enter password")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "is_deleted")
    private boolean deleted;
}
