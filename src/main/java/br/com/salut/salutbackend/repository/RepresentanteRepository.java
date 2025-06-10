package br.com.salut.salutbackend.repository;
import br.com.salut.salutbackend.model.Representante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RepresentanteRepository extends JpaRepository<Representante, Long> {
    Representante findByEmail(String email);
}