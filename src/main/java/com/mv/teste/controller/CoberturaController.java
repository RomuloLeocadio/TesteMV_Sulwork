package com.mv.teste.controller;

import com.mv.teste.dto.CoberturaEvento;
import com.mv.teste.service.CoberturaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eventos")
public class CoberturaController {

    private final CoberturaService service;

    public CoberturaController(CoberturaService service) {
        this.service = service;
    }

    @GetMapping("/{eventoId}/cobertura")
    public CoberturaEvento cobertura(@PathVariable("eventoId") Long eventoId) {
        return service.calcular(eventoId);
    }
}