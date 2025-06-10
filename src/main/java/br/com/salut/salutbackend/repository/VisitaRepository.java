package br.com.salut.salutbackend.repository;

import br.com.salut.salutbackend.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    // Encontrar todas as visitas de um representante específico
    List<Visita> findByRepresentanteId(Long representanteId);

    // Encontrar todas as visitas para um cliente específico
    List<Visita> findByClienteId(Long clienteId);
}