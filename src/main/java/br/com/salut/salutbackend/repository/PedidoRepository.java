package br.com.salut.salutbackend.repository;

import br.com.salut.salutbackend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // NOVO IMPORT

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // ADICIONE APENAS ESTA LINHA
    List<Pedido> findByClienteId(Long clienteId);
}