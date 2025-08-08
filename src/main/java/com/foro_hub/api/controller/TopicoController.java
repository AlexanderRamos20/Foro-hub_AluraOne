package com.foro_hub.api.controller;

import com.foro_hub.api.domain.topico.TopicoDTO;
import com.foro_hub.api.domain.topico.TopicoRepository;
import com.foro_hub.api.domain.usuario.DetallesAutorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @GetMapping
    public ResponseEntity<Page<TopicoDTO>> listar (@PageableDefault(size = 5, sort = {"fechaCreacion"})Pageable paginacion){
        var page = repository.findAll(paginacion)
                .map(topico -> new TopicoDTO(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getFechaCreacion(),
                        new DetallesAutorDTO(topico.getAutor().getNombre())
                ));
        return ResponseEntity.ok(page);
    }
}
