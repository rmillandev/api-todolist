package com.app.todolist.security;


import com.app.todolist.model.Users;
import com.app.todolist.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUsersDetailsService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    // Metodo para traer rol, en este caso no habra roles en la db asi que sera manual, para que no halla problemas
    public Collection<GrantedAuthority> mapToAuthorities(){
        // Se devuelve una autoridad fija
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Metodo para traer los datos del usuario por el username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(users.getUsername(), users.getPassword(), mapToAuthorities());
    }

}
