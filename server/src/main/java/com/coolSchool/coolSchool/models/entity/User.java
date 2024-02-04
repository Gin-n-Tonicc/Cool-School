package com.coolSchool.coolSchool.models.entity;

import com.coolSchool.coolSchool.enums.Provider;
import com.coolSchool.coolSchool.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The name should not be null!")
    private String firstname;

    @NotNull(message = "The name should not be null!")
    private String lastname;

    @Email(message = "Email should be a well-formatted email!")
    @NotNull(message = "The email should not be null!")
    @Column(unique = true)
    private String email;

    private String password;

    @NotNull(message = "The address should not be null!")
    private String address;

    @NotNull(message = "The username should not be null!")
    @Column(name = "username")
    private String usernameField;

    @NotNull(message = "The description shot not be null!")
    @Size(min = 60, max = 120, message = "The description must be between 60 and 120 symbols!")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File profilePic;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "is_additional_info_required", nullable = false)
    private boolean additionalInfoRequired;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
    @Column(name = "enabled")
    private boolean enabled;
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @PrePersist
    void prePersist() {
        if (this.provider == null) {
            this.provider = Provider.LOCAL;
        }
    }
}
