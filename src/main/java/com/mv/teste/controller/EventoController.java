package com.mv.teste.controller;

import com.mv.teste.dto.EventoRequest;
import com.mv.teste.dto.EventoResponse;
import com.mv.teste.model.StatusEvento;
import com.mv.teste.service.EventoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponse criar(@RequestBody EventoRequest req) {
        return EventoResponse.de(service.criar(req));
    }

    @GetMapping
    public List<EventoResponse> listar() {
        return service.listar().stream().map(EventoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public EventoResponse buscar(@PathVariable("id") Long id) {
        return EventoResponse.de(service.buscar(id));
    }

    @PutMapping("/{id}")
    public EventoResponse atualizar(@PathVariable("id") Long id, @RequestBody EventoRequest req) {
        return EventoResponse.de(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/status")
    public EventoResponse alterarStatus(@PathVariable("id") Long id,
                                        @RequestParam("status") StatusEvento status) {
        return EventoResponse.de(service.alterarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}