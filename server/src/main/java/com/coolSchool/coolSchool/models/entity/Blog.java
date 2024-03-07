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
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The title of the blog should not be null!")
    @NotBlank(message = "The title of the blog should not be blank!")
    @Size(min = 5, max = 50, message = "The title must be at least 5 symbols and less than 50!")
    private String title;

    @NotNull(message = "The content of the blog should not be null!")
    @NotBlank(message = "The content of the blog should not be blank!")
    @Size(min = 150, message = "The content must be at least 150 symbols!")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;
    @NotNull(message = "The summary of the blog should not be null!")
    @NotBlank(message = "The summary of the blog should not be blank!")
    @Size(min = 10, message = "The summary must be at least 10 symbols!")
    private String summary;
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @ManyToMany
    private Set<User> liked_users;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File picture;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User ownerId;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "The category of the blog should not be null!")
    private Category categoryId;
    private boolean isEnabled;
    private Integer commentCount;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
