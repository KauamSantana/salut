package br.com.salut.salutbackend.repository;
import br.com.salut.salutbackend.model.Vinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VinhoRepository extends JpaRepository<Vinho, Long> {}