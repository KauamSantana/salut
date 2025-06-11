package br.com.salut.salutbackend.service;

import br.com.salut.salutbackend.dto.DashboardDTO;
import br.com.salut.salutbackend.model.Representante;
import br.com.salut.salutbackend.model.Visita;
import br.com.salut.salutbackend.repository.PedidoRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import br.com.salut.salutbackend.repository.VisitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class DashboardService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RepresentanteRepository representanteRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    public DashboardDTO getDashboardInfo(Long representanteId) {
        Representante representante = representanteRepository.findById(representanteId)
                .orElseThrow(() -> new RuntimeException("Representante não encontrado"));

        // 1. Contar Pedidos Pendentes
        long pedidosPendentes = pedidoRepository.countByRepresentanteIdAndStatus(representanteId, "PENDENTE");

        // 2. Calcular Percentual da Meta do Mês
        LocalDateTime inicioDoMes = YearMonth.now().atDay(1).atStartOfDay();
        LocalDateTime fimDoMes = YearMonth.now().atEndOfMonth().atTime(23, 59, 59);
        BigDecimal vendasNoMes = pedidoRepository.calcularTotalVendasPorRepresentanteNoPeriodo(representanteId, inicioDoMes, fimDoMes);
        vendasNoMes = (vendasNoMes == null) ? BigDecimal.ZERO : vendasNoMes;
        BigDecimal meta = representante.getMeta() != null ? representante.getMeta() : BigDecimal.ZERO;
        BigDecimal percentualMetaMes = BigDecimal.ZERO;
        if (meta.compareTo(BigDecimal.ZERO) > 0) {
            percentualMetaMes = vendasNoMes.multiply(new BigDecimal("100")).divide(meta, 2, RoundingMode.HALF_UP);
        }

        // 3. Calcular Comissão Total
        BigDecimal totalVendidoGeral = pedidoRepository.calcularTotalVendasPorRepresentante(representanteId);
        totalVendidoGeral = (totalVendidoGeral == null) ? BigDecimal.ZERO : totalVendidoGeral;
        BigDecimal taxaComissao = representante.getTaxaComissao() != null ? representante.getTaxaComissao() : BigDecimal.ZERO;
        BigDecimal valorComissaoTotal = BigDecimal.ZERO;
        if (taxaComissao.compareTo(BigDecimal.ZERO) > 0) {
            valorComissaoTotal = totalVendidoGeral.multiply(taxaComissao.divide(new BigDecimal("100")));
        }

        // 4. Buscar Próxima Visita (Rota)
        Visita proximaVisita = visitaRepository
                .findTopByRepresentanteIdAndDataHoraAfterAndStatusOrderByDataHoraAsc(representanteId, LocalDateTime.now(), "AGENDADA")
                .orElse(null);

        return new DashboardDTO(pedidosPendentes, percentualMetaMes, valorComissaoTotal, proximaVisita);
    }
}