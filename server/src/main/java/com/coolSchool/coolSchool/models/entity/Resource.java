package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The name of the resource should not be null!")
    private String name;
    @ManyToOne
    @JoinColumn(name = "file_id")
    @NotNull(message = "The file of the resource should not be null!")
    private File file;
    @ManyToOne
    @JoinColumn(name = "subsection_id")
    @NotNull(message = "The subsection of the resource should not be null!")
    private CourseSubsection subsection;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
