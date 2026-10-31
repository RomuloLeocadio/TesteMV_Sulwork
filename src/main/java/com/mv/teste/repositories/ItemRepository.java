package com.mv.teste.repositories;

import com.mv.teste.model.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategoriaId(Long categoriaId);
}