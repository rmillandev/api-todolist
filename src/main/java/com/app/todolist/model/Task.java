package com.app.todolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    @NotBlank(message = "La descripcion no puede estar en blanco")
    @NotNull(message = "La descripcion no puede ir nula")
    private String description;

    @Column(name = "state")
    private Boolean state = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
