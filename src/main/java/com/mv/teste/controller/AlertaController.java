package com.mv.teste.controller;

import com.mv.teste.dto.AlertaAlergeno;
import com.mv.teste.service.AlertaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eventos")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @GetMapping("/{eventoId}/alertas-alergenos")
    public List<AlertaAlergeno> alertas(@PathVariable("eventoId") Long eventoId) {
        return service.calcular(eventoId);
    }
}