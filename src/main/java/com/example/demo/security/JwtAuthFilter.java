package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraer el Header "Authorization"
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtils.validarToken(token)) {
                String correo = jwtUtils.extraerCorreo(token);
                // Aquí extraemos el rol desde el token para autorizar la ruta
                String rol = Jwts.parserBuilder()
                                 .setSigningKey(jwtUtils.getSigningKey())
                                 .build()
                                 .parseClaimsJws(token)
                                 .getBody()
                                 .get("rol", String.class);

                // Autenticar al usuario en el contexto de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        correo, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase())));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continuar con la petición
        filterChain.doFilter(request, response);
    }
}