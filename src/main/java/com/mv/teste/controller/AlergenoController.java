package com.mv.teste.controller;

import com.mv.teste.dto.AlergenoRequest;
import com.mv.teste.dto.AlergenoResponse;
import com.mv.teste.service.AlergenoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alergenos")
public class AlergenoController {

    private final AlergenoService service;

    public AlergenoController(AlergenoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlergenoResponse criar(@RequestBody AlergenoRequest req) {
        return AlergenoResponse.de(service.criar(req));
    }

    @GetMapping
    public List<AlergenoResponse> listar() {
        return service.listar().stream().map(AlergenoResponse::de).toList();
    }
}