package com.foro_hub.api.domain.topico;

import com.foro_hub.api.domain.usuario.DetallesAutorDTO;
import com.foro_hub.api.domain.usuario.Usuario;

import java.time.LocalDateTime;

public record TopicoDTO(
        Long id,
        String titulo,
        LocalDateTime fechaCreacion,
        DetallesAutorDTO autor,
        boolean activo
) {
    public TopicoDTO (Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getFechaCreacion(),
                mapAutor(topico.getAutor()),
                topico.isActivo()
        );
    }

    public static DetallesAutorDTO mapAutor (Usuario autor){
        return (autor == null) ? null: new DetallesAutorDTO(autor.getNombre());
    }
}
