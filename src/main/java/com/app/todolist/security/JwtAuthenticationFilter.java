package com.app.todolist.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// La funcion de esta clase sera validar la informacion del token y si esto es exitoso, establecera la autenticacion de un usuario en la solicitud
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUsersDetailsService customUsersDetailsService;

    @Autowired
    private JwtGenerador jwtGenerador;

    /*Con el siguiente método extraeremos  el token JWT de la cabecera de nuestra petición Http("Authorization")
     * luego lo validaremos y finalmente se retornará*/
    private String obtenerTokenDeSolicitud(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            //Aca si se encuentra el token JWT, se devuelve una subcadena de "bearerToken" que comienza después de los primeros 7 caracteres hasta el final de la cadena
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = obtenerTokenDeSolicitud(request);

        if(StringUtils.hasText(token) && jwtGenerador.validarToken(token)){
            String username = jwtGenerador.obtenerUsernameDeJwt(token);
            UserDetails userDetails = customUsersDetailsService.loadUserByUsername(username);

            if(!userDetails.getAuthorities().isEmpty()){
                // Si el usuario tiene alguna autoridad, crea la autenticación y establece el contexto de seguridad
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
