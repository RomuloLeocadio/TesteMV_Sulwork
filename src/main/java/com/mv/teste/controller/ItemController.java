package com.mv.teste.controller;

import com.mv.teste.dto.ItemRequest;
import com.mv.teste.dto.ItemResponse;
import com.mv.teste.service.ItemService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping("/categorias/{categoriaId}/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse criar(@PathVariable("categoriaId") Long categoriaId, @RequestBody ItemRequest req) {
        return ItemResponse.de(service.criar(categoriaId, req));
    }

    @GetMapping("/categorias/{categoriaId}/itens")
    public List<ItemResponse> listar(@PathVariable("categoriaId") Long categoriaId) {
        return service.listarPorCategoria(categoriaId).stream().map(ItemResponse::de).toList();
    }

    @PutMapping("/itens/{id}")
    public ItemResponse atualizar(@PathVariable("id") Long id, @RequestBody ItemRequest req) {
        return ItemResponse.de(service.atualizar(id, req));
    }

    @DeleteMapping("/itens/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") Long id) {
        service.excluir(id);
    }
}