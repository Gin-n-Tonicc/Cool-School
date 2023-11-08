package com.coolSchool.CoolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String name;
    @NotNull(message = "The name of the class should not be null!")
    private String aClass;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "The user of the class should not be null!")
    private User user;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "The category of the class should not be null!")
    private Category category;
    @Column(columnDefinition = "DECIMAL(10, 2)")
    private double stars;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
