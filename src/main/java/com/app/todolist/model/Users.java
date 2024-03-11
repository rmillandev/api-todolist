package com.app.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",  uniqueConstraints = {@UniqueConstraint(columnNames = {"dni"}), @UniqueConstraint(columnNames = {"username"})})
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "dni", unique = true)
    @NotNull(message = "El DNI no puede ir vacio")
    private long dni;

    @Column(name = "full_name")
    @NotBlank(message = "El Nombre Completo no puede estar vacio")
    private String fullName;

    @Column(name = "username")
    @NotBlank(message = "El Username no puede estar vacio")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "La password no puede estar en blanco")
    private String password;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Task> taskList;

}
