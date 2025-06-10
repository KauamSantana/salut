package br.com.salut.salutbackend.repository;
import br.com.salut.salutbackend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}