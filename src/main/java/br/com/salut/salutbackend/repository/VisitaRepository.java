package br.com.salut.salutbackend.repository;

import br.com.salut.salutbackend.model.Visita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    // MUDANÇA AQUI: de List para Page
    Page<Visita> findByRepresentanteId(Long representanteId, Pageable pageable);
    Page<Visita> findByClienteId(Long clienteId, Pageable pageable);

    Optional<Visita> findTopByRepresentanteIdAndDataHoraAfterAndStatusOrderByDataHoraAsc(Long representanteId, LocalDateTime dataAtual, String status);
}