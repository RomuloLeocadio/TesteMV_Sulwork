package com.mv.teste.repositories;

import com.mv.teste.model.Alergeno;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// @Repository
public interface AlergenoRepository extends JpaRepository<Alergeno, Long> {
}