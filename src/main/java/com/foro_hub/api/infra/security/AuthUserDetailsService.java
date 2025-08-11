package com.foro_hub.api.infra.security;

import com.foro_hub.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    public AuthUserDetailsService(UsuarioRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = repository.findByCorreo(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));

        return new UsuarioDetails(usuario);
    }
}
