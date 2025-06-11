package br.com.salut.salutbackend.repository;

import br.com.salut.salutbackend.model.Pedido;
import org.springframework.data.domain.Page; // NOVO IMPORT
import org.springframework.data.domain.Pageable; // NOVO IMPORT
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // MUDANÇA AQUI: de List para Page
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);

    // MUDANÇA AQUI: de List para Page
    Page<Pedido> findByStatus(String status, Pageable pageable);

    // NOVO MÉTODO PARA CONTAR PEDIDOS
    long countByRepresentanteIdAndStatus(Long representanteId, String status);

    // NOVO MÉTODO PARA CALCULAR VENDAS DO REPRESENTANTE EM UM PERÍODO
    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.representante.id = :representanteId AND p.dataDoPedido BETWEEN :inicio AND :fim")
    BigDecimal calcularTotalVendasPorRepresentanteNoPeriodo(@Param("representanteId") Long representanteId, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.cliente.id = :clienteId")
    BigDecimal calcularTotalVendasPorCliente(@Param("clienteId") Long clienteId);

    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.representante.id = :representanteId")
    BigDecimal calcularTotalVendasPorRepresentante(@Param("representanteId") Long representanteId);

    @Query("SELECT SUM(i.precoUnitario * i.quantidade) FROM Pedido p JOIN p.itens i WHERE p.dataDoPedido BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalVendasPorPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}