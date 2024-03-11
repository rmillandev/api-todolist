package com.app.todolist.service;

import com.app.todolist.model.Users;
import com.app.todolist.repository.IUserRepository;
import com.app.todolist.security.ConstantesSeguridad;
import com.app.todolist.security.JwtGenerador;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtGenerador jwtGenerador;

    private HashMap<String, Object> data;

    public ResponseEntity<?> login(LoginDTO loginDTO){
        data = new HashMap<>();

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Users userData = userRepository.findByUsername(loginDTO.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            int id = userData.getId();
            String fullname = userData.getFullName();
            long dni = userData.getDni();

            Date tiempoActual = new Date();
            Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

            String token = jwtGenerador.generarToken(authentication);

            data.put("message", "Inicio de sesion exitoso");
            data.put("body", new AuthResponseDTO(token,expiracionToken,id,fullname,dni));

            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (AuthenticationException e) {
            // Manejar el error de autenticación
            data.put("error", e.getMessage() + ": Escriba correctamente sus credenciales");
            return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

}
