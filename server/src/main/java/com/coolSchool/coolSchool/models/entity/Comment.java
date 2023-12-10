package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The content of the comment should not be null!")
    @NotBlank(message = "The content of the comment should not be blank!")
    @Size(max = 200, message = "The comment must be less than 200 symbols!")
    private String comment;
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ownerId;
    @ManyToOne
    @JoinColumn(name = "blog_id")
    @NotNull(message = "The blog of the comment should not be null!")
    private Blog blogId;
    @ManyToMany
    private Set<User> liked_users;
    private boolean deleted;
}
