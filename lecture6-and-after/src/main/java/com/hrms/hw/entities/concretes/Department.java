package com.hrms.hw.entities.concretes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_id_generator")
    @SequenceGenerator(name = "departments_id_generator", sequenceName = "departments_id_seq", allocationSize = 1)
    @JsonIgnore
    @Column(name = "id")
    private short id;

    @NotBlank(message = "cannot be empty")
    @Pattern(regexp = "\\w+", message = "invalid department name")
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
