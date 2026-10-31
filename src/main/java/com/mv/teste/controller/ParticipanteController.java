package com.mv.teste.controller;

import com.mv.teste.dto.ParticipanteRequest;
import com.mv.teste.dto.ParticipanteResponse;
import com.mv.teste.service.ParticipanteService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ParticipanteController {

    private final ParticipanteService service;

    public ParticipanteController(ParticipanteService service) {
        this.service = service;
    }

    @PostMapping("/participantes")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipanteResponse criar(@RequestBody ParticipanteRequest req) {
        return ParticipanteResponse.de(service.criar(req));
    }

    @GetMapping("/participantes")
    public List<ParticipanteResponse> listar() {
        return service.listar().stream().map(ParticipanteResponse::de).toList();
    }

    @GetMapping("/participantes/{id}")
    public ParticipanteResponse buscar(@PathVariable("id") Long id) {
        return ParticipanteResponse.de(service.buscar(id));
    }

    @PutMapping("/participantes/{id}")
    public ParticipanteResponse atualizar(@PathVariable("id") Long id, @RequestBody ParticipanteRequest req) {
        return ParticipanteResponse.de(service.atualizar(id, req));
    }

    @DeleteMapping("/participantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }

    @PostMapping("/eventos/{eventoId}/participantes/{participanteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void inscrever(@PathVariable("eventoId") Long eventoId,
                          @PathVariable("participanteId") Long participanteId) {
        service.inscrever(eventoId, participanteId);
    }

    @GetMapping("/eventos/{eventoId}/participantes")
    public List<ParticipanteResponse> listarPorEvento(@PathVariable("eventoId") Long eventoId) {
        return service.listarPorEvento(eventoId).stream().map(ParticipanteResponse::de).toList();
    }
}