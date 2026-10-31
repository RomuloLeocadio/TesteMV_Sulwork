package com.mv.teste.controller;

import com.mv.teste.dto.RestricaoRequest;
import com.mv.teste.dto.RestricaoResponse;
import com.mv.teste.service.RestricaoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestricaoController {

    private final RestricaoService service;

    public RestricaoController(RestricaoService service) {
        this.service = service;
    }

    @PostMapping("/participantes/{participanteId}/restricoes")
    @ResponseStatus(HttpStatus.CREATED)
    public RestricaoResponse criar(@PathVariable("participanteId") Long participanteId,
                                   @RequestBody RestricaoRequest req) {
        return RestricaoResponse.de(service.criar(participanteId, req));
    }

    @GetMapping("/participantes/{participanteId}/restricoes")
    public List<RestricaoResponse> listar(@PathVariable("participanteId") Long participanteId) {
        return service.listarPorParticipante(participanteId).stream().map(RestricaoResponse::de).toList();
    }

    @DeleteMapping("/restricoes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}