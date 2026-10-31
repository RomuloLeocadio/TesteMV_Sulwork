package com.mv.teste.repositories;

import com.mv.teste.model.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByEventoId(Long eventoId);
}
