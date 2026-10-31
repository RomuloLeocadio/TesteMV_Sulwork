package com.mv.teste.controller;

import com.mv.teste.dto.ContribuicaoRequest;
import com.mv.teste.dto.ContribuicaoResponse;
import com.mv.teste.service.ContribuicaoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ContribuicaoController {

    private final ContribuicaoService service;

    public ContribuicaoController(ContribuicaoService service) {
        this.service = service;
    }

    @PostMapping("/itens/{itemId}/contribuicoes")
    @ResponseStatus(HttpStatus.CREATED)
    public ContribuicaoResponse criar(@PathVariable("itemId") Long itemId, @RequestBody ContribuicaoRequest req) {
        return ContribuicaoResponse.de(service.criar(itemId, req));
    }

    @GetMapping("/itens/{itemId}/contribuicoes")
    public List<ContribuicaoResponse> listar(@PathVariable("itemId") Long itemId) {
        return service.listarPorItem(itemId).stream().map(ContribuicaoResponse::de).toList();
    }

    @PutMapping("/contribuicoes/{id}")
    public ContribuicaoResponse atualizar(@PathVariable("id") Long id, @RequestBody ContribuicaoRequest req) {
        return ContribuicaoResponse.de(service.atualizar(id, req));
    }

    @DeleteMapping("/contribuicoes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}