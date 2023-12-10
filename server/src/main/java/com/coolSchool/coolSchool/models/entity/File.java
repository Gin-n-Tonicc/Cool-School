package com.coolSchool.coolSchool.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "The name of the file should not be null!")
    private String name;
    @NotNull(message = "The url of the file should not be null!")
    private String url;
    @NotNull
    private String type;
    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;
}
