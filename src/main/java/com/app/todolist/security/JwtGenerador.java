package com.app.todolist.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerador {

    // Metodo para crear un token por medio de la authenticacion
    public String generarToken(Authentication authentication){
        String username = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        // Linea para generar el token
        String token = Jwts.builder() //Construimos un token JWT llamado token
                .setSubject(username) //Aca establecemos el nombre de usuario que esta iniciando sesion
                .setIssuedAt(new Date()) // Establecemos la fecha de emision del token
                .setExpiration(expiracionToken) // Establecemos fecha de caducuidad del token
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA) /*Utilizamos este método para firmar
                nuestro token y de esta manera evitar la manipulación o modificación de este*/
                .compact(); //Este método finaliza la construcción del token y lo convierte en una cadena compacta
        return token;
    }

    //Metodo para extraer un Username apartir de un token
    public String obtenerUsernameDeJwt(String token){
        return Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token).getBody().getSubject();
    }

    // Metodo para validar el token
    public Boolean validarToken(String token){
        try{
            //Validación del token por medio de la firma que contiene el String token(token)
            //Si son idénticas validara el token o caso contrario saltara la excepción de abajo
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        }catch (Exception ex){
            throw new AuthenticationCredentialsNotFoundException("Jwt ah expirado o esta incorrecto");
        }
    }

}
