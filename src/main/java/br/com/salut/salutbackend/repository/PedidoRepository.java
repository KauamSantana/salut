package br.com.salut.salutbackend.repository;

import br.com.salut.salutbackend.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime; // NOVO IMPORT

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Métodos que já tínhamos
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(String status);

    // Nossos novos métodos para os relatórios
    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.cliente.id = :clienteId")
    BigDecimal calcularTotalVendasPorCliente(@Param("clienteId") Long clienteId);

    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.representante.id = :representanteId")
    BigDecimal calcularTotalVendasPorRepresentante(@Param("representanteId") Long representanteId);
    // NOVO MÉTODO PARA CALCULAR VENDAS POR PERÍODO

    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.dataDoPedido BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalVendasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}