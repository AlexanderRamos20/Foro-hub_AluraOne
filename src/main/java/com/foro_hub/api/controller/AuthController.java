package com.foro_hub.api.controller;

import com.foro_hub.api.infra.security.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public AuthController(
            AuthenticationManager authManager,
            TokenService tokenService,
            UserDetailsService userDetailsService
    ) {
        this.authManager = authManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    // Record local solo para este controller (sin crear DTO aparte)
    private record Login(String correo, String clave) {}

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Login body) {
        try {
            // Autenticar con correo + clave
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.correo(), body.clave())
            );

            // Cargar el usuario y generar token
            UserDetails user = userDetailsService.loadUserByUsername(body.correo());
            String token = tokenService.generarToken(user);

            // Respuesta simple
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "type", "Bearer"
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }
}

