package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.VendasPorRegiaoDTO; // Adicionado
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.repository.PedidoRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List; // Adicionado
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RepresentanteRepository representanteRepository;

    // --- O MÉTODO QUE FALTAVA ---
    @GetMapping("/vendas/por-regiao")
    public ResponseEntity<List<VendasPorRegiaoDTO>> getVendasPorRegiao() {
        List<VendasPorRegiaoDTO> relatorio = pedidoRepository.findVendasPorRegiao();
        return ResponseEntity.ok(relatorio);
    }
    // ----------------------------

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

    @GetMapping("/performance/por-representante")
    public ResponseEntity<Map<String, Object>> getPerformancePorRepresentante(@RequestParam Long representanteId) {
        Representante representante = representanteRepository.findById(representanteId)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado"));

        BigDecimal totalVendido = pedidoRepository.calcularTotalVendasPorRepresentante(representanteId);
        totalVendido = (totalVendido == null) ? BigDecimal.ZERO : totalVendido;

        BigDecimal meta = representante.getMeta() == null ? BigDecimal.ZERO : representante.getMeta();
        BigDecimal taxaComissao = representante.getTaxaComissao() == null ? BigDecimal.ZERO : representante.getTaxaComissao();
        BigDecimal valorComissao = BigDecimal.ZERO;
        BigDecimal percentualAtingido = BigDecimal.ZERO;

        if (meta.compareTo(BigDecimal.ZERO) > 0) {
            percentualAtingido = totalVendido.multiply(new BigDecimal("100")).divide(meta, 2, RoundingMode.HALF_UP);
        }

        if (taxaComissao.compareTo(BigDecimal.ZERO) > 0) {
            valorComissao = totalVendido.multiply(taxaComissao.divide(new BigDecimal("100")));
        }

        Map<String, Object> response = Map.of(
                "representanteId", representante.getId(),
                "nomeRepresentante", representante.getNome(),
                "meta", meta,
                "totalVendido", totalVendido,
                "percentualAtingido", percentualAtingido + "%",
                "taxaComissao", taxaComissao + "%",
                "valorComissao", valorComissao
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendas/por-periodo")
    public ResponseEntity<Map<String, Object>> getVendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        BigDecimal totalVendas = pedidoRepository.calcularTotalVendasPorPeriodo(dataInicio, dataFim);
        totalVendas = (totalVendas == null) ? BigDecimal.ZERO : totalVendas;

        Map<String, Object> response = Map.of(
                "dataInicio", dataInicio,
                "dataFim", dataFim,
                "totalVendasNoPeriodo", totalVendas
        );
        return ResponseEntity.ok(response);
    }
}