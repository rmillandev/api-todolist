package com.app.todolist.controller;

import com.app.todolist.repository.IUserRepository;
import com.app.todolist.security.JwtGenerador;
import com.app.todolist.service.AuthService;
import com.app.todolist.util.AuthResponseDTO;
import com.app.todolist.util.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return service.login(loginDTO);
    }


}
