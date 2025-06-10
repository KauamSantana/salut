package br.com.salut.salutbackend.repository;

import br.com.salut.salutbackend.model.Vinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // NOVO IMPORT

@Repository
public interface VinhoRepository extends JpaRepository<Vinho, Long> {

    // ADICIONE APENAS ESTA LINHA
    List<Vinho> findByTipo(String tipo);
}