package com.foro_hub.api.domain.topico;

import com.foro_hub.api.domain.usuario.DetallesAutorDTO;

import java.time.LocalDateTime;

public record TopicoDTO(
        Long id,
        String titulo,
        LocalDateTime fechaCreacion,
        DetallesAutorDTO autor
) {}
