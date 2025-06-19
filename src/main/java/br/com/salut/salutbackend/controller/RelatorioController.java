package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.VendasPorRegiaoDTO;
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
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private PedidoRepository pedidoRepository;

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

    @GetMapping("/performance/por-representante")
    public ResponseEntity<Map<String, Object>> getPerformancePorRepresentante(
            @RequestParam Long representanteId,
            @RequestParam Optional<Integer> ano,
            @RequestParam Optional<Integer> mes) {

        Representante representante = representanteRepository.findById(representanteId)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado"));

        YearMonth anoMes = (ano.isPresent() && mes.isPresent())
                ? YearMonth.of(ano.get(), mes.get())
                : YearMonth.now();

        LocalDateTime inicioDoPeriodo = anoMes.atDay(1).atStartOfDay();
        LocalDateTime fimDoPeriodo = anoMes.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal vendasNoPeriodo = pedidoRepository.calcularTotalVendasPorRepresentanteNoPeriodo(representanteId, inicioDoPeriodo, fimDoPeriodo);
        vendasNoPeriodo = (vendasNoPeriodo == null) ? BigDecimal.ZERO : vendasNoPeriodo;

        BigDecimal totalVendidoGeral = pedidoRepository.calcularTotalVendasPorRepresentante(representanteId);
        totalVendidoGeral = (totalVendidoGeral == null) ? BigDecimal.ZERO : totalVendidoGeral;

        BigDecimal meta = representante.getMeta() != null ? representante.getMeta() : BigDecimal.ZERO;
        BigDecimal taxaComissao = representante.getTaxaComissao() != null ? representante.getTaxaComissao() : BigDecimal.ZERO;
        BigDecimal valorComissao = BigDecimal.ZERO;
        BigDecimal percentualAtingido = BigDecimal.ZERO;

        if (meta.compareTo(BigDecimal.ZERO) > 0) {
            percentualAtingido = vendasNoPeriodo.multiply(new BigDecimal("100")).divide(meta, 2, RoundingMode.HALF_UP);
        }

        if (taxaComissao.compareTo(BigDecimal.ZERO) > 0) {
            valorComissao = totalVendidoGeral.multiply(taxaComissao.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
        }

        Map<String, Object> response = Map.of(
                "periodo", anoMes.toString(),
                "representanteId", representante.getId(),
                "nomeRepresentante", representante.getNome(),
                "meta", meta,
                "totalVendidoNoPeriodo", vendasNoPeriodo,
                "percentualAtingido", percentualAtingido + "%",
                "taxaComissao", taxaComissao + "%",
                "valorComissaoTotal", valorComissao
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

    @GetMapping("/vendas/por-regiao")
    public ResponseEntity<List<VendasPorRegiaoDTO>> getVendasPorRegiao() {
        List<VendasPorRegiaoDTO> relatorio = pedidoRepository.findVendasPorRegiao();
        return ResponseEntity.ok(relatorio);
    }
}