package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The name of the course should not be null!")
    @Size(min = 10, message = "The name must be at least 10 symbols!")
    private String name;
    @NotNull(message = "The objectives of the course should not be null!")
    @Size(min = 150, message = "The objectives must be at least 150 symbols!")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String objectives;
    @NotNull(message = "The eligibility of the course should not be null!")
    @Size(min = 150, message = "The eligibility must be at least 150 symbols!")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String eligibility;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The trainer of the course should not be null!")
    private User user;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File picture;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "The category of the class should not be null!")
    private Category category;
    @Column(columnDefinition = "DECIMAL(10, 2)")
    private double stars;
    @Column(name = "created_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}
