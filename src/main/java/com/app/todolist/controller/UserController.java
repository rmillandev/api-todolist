package com.app.todolist.controller;

import com.app.todolist.model.Users;
import com.app.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody @Valid Users users, BindingResult result){
        return service.register(users, result);
    }

    @PutMapping(value = "/updatePassword/{id}")
    public ResponseEntity<Object> updatePassword(@PathVariable int id, @RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword){
        return service.updatePassword(id, currentPassword, newPassword);
    }

}
