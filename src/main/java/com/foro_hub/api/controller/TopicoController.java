package com.foro_hub.api.controller;

import com.foro_hub.api.domain.topico.*;
import com.foro_hub.api.domain.usuario.DetallesAutorDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

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
                        new DetallesAutorDTO(topico.getAutor().getNombre()),
                        topico.isActivo()
                ));
        return ResponseEntity.ok(page);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<TopicoDTO> crearTopico(@RequestBody @Valid DatosCrearTopico datos, UriComponentsBuilder uriComponentsBuilder){
        var topico = new Topico(datos);
        repository.save(topico);

        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTopico (@PathVariable Long id){
        var opt = repository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).build(); // fuerza 404
        }
        var topico = opt.get();
        topico.setActivo(false);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TopicoDTO> modificarTopico (@RequestBody DatosActualizarTopico datos, @PathVariable Long id){
        var topico = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        if (datos.titulo() != null && !datos.titulo().isBlank()){
            topico.setTitulo(datos.titulo());
        }

        if(datos.mensaje() != null && !datos.titulo().isBlank()){
            topico.setMensaje(datos.mensaje());
        }

        return ResponseEntity.ok(new TopicoDTO(topico));
    }

}
