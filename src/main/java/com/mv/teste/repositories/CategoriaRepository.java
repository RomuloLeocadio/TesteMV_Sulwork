package com.mv.teste.repositories;

import com.mv.teste.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
