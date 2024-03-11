package com.app.todolist.service;

import com.app.todolist.model.Users;
import com.app.todolist.repository.IUserRepository;
import com.app.todolist.util.EncryptPassword;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private EncryptPassword encrypt;

    private HashMap<String, Object> data;

    public ResponseEntity<Object> register(@Valid Users users, BindingResult result){
        data = new HashMap<>();

        if(result.hasErrors()){
            List<String> errors = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errors.add(error.getDefaultMessage());
            }
            data.put("message", "No se pudo crear la tarea.");
            data.put("error", errors);
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            // Se encripta la password
            String encryptedPassword = encrypt.encryptPassword(users.getPassword());

            // Se asigna la password encriptada
            users.setPassword(encryptedPassword);

            // Se guarda el usuario
            Users savedUsers = userRepository.save(users);

            data.put("message", "Registro creado correctamente");
            data.put("body", savedUsers);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex){
            data.put("error", "No se pudo registrar. El DNI ya esta registrado.");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            data.put("error", "No se pudo registrar al usuario " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar contrasena
    public ResponseEntity<Object> updatePassword(int id, String currentPassword, String newPassword){
        data = new HashMap<>();

        Users users = userRepository.findById(id).orElse(null);

        if(users == null){
            data.put("message", "Usuario no encontrado");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        String encryptedCurrentPassword = users.getPassword();
        if(!encrypt.validatePassword(currentPassword, encryptedCurrentPassword)){
            data.put("message", "La contraseña actual no coincide con la que estas ingresando");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        String encryptedNewPassword = encrypt.encryptPassword(newPassword);
        users.setPassword(encryptedNewPassword);
        userRepository.save(users);

        data.put("message", "Contraseña actualizada correctamente");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
