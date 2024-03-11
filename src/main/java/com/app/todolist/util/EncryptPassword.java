package com.app.todolist.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncryptPassword {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encryptPassword (String password){
        return passwordEncoder.encode(password);
    }

    public boolean validatePassword(String rawPassword, String encodePassword){
        return passwordEncoder.matches(rawPassword, encodePassword);
    }

}
