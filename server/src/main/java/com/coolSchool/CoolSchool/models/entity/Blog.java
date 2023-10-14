package com.coolSchool.CoolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    @NotNull(message = "The title of the blog should not be blank!")
    private String title;
    @NotNull(message = "The content of the blog should not be null!")
    @NotNull(message = "The content of the blog should not be blank!")
    private String content;
    @NotNull(message = "The summary of the blog should not be null!")
    @NotNull(message = "The summary of the blog should not be blank!")
    private String summary;
    @NotNull(message = "The created time of the blog should not be null!")
    private LocalDateTime created_at;
    @OneToMany
    private List<User> liked_users;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File picture;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The ownerId of the blog should not be null!")
    private User ownerId;
    private boolean deleted;
}
