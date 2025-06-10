package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.PedidoRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // NOVO REPOSITÓRIO NECESSÁRIO
    @Autowired
    private RepresentanteRepository representanteRepository;

    @GetMapping("/vendas/por-cliente")
    public ResponseEntity<Map<String, Object>> getVendasPorCliente(@RequestParam Long clienteId) {
        BigDecimal totalVendas = pedidoRepository.calcularTotalVendasPorCliente(clienteId);
        totalVendas = (totalVendas == null) ? BigDecimal.ZERO : totalVendas;
        Map<String, Object> response = Map.of(
                "clienteId", clienteId,
                "totalVendas", totalVendas
        );
        return ResponseEntity.ok(response);
    }

    // NOVO ENDPOINT DE PERFORMANCE
    @GetMapping("/performance/por-representante")
    public ResponseEntity<Map<String, Object>> getPerformancePorRepresentante(@RequestParam Long representanteId) {
        // Busca o representante para pegar a meta
        Representante representante = representanteRepository.findById(representanteId)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado"));

        // Calcula o total vendido
        BigDecimal totalVendido = pedidoRepository.calcularTotalVendasPorRepresentante(representanteId);
        totalVendido = (totalVendido == null) ? BigDecimal.ZERO : totalVendido;

        BigDecimal meta = representante.getMeta() == null ? BigDecimal.ZERO : representante.getMeta();
        BigDecimal percentualAtingido = BigDecimal.ZERO;

        // Calcula o percentual atingido, evitando divisão por zero
        if (meta.compareTo(BigDecimal.ZERO) > 0) {
            percentualAtingido = totalVendido.multiply(new BigDecimal("100")).divide(meta, 2, RoundingMode.HALF_UP);
        }

        Map<String, Object> response = Map.of(
                "representanteId", representante.getId(),
                "nomeRepresentante", representante.getNome(),
                "meta", meta,
                "totalVendido", totalVendido,
                "percentualAtingido", percentualAtingido + "%"
        );
        return ResponseEntity.ok(response);
    }
}