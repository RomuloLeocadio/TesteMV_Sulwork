package com.mv.teste.repositories;

import com.mv.teste.model.RestricaoAlimentar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestricaoAlimentarRepository extends JpaRepository<RestricaoAlimentar, Long> {

    List<RestricaoAlimentar> findByParticipanteId(Long participanteId);
}