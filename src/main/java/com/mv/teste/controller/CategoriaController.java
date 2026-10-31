package com.mv.teste.controller;

import com.mv.teste.dto.CategoriaRequest;
import com.mv.teste.dto.CategoriaResponse;
import com.mv.teste.service.CategoriaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping("/eventos/{eventoId}/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse criar(@PathVariable("eventoId") Long eventoId, @RequestBody CategoriaRequest req) {
        return CategoriaResponse.de(service.criar(eventoId, req));
    }

    @GetMapping("/eventos/{eventoId}/categorias")
    public List<CategoriaResponse> listar(@PathVariable("eventoId") Long eventoId) {
        return service.listarPorEvento(eventoId).stream().map(CategoriaResponse::de).toList();
    }

    @PutMapping("/categorias/{id}")
    public CategoriaResponse atualizar(@PathVariable("id") Long id, @RequestBody CategoriaRequest req) {
        return CategoriaResponse.de(service.atualizar(id, req));
    }

    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}