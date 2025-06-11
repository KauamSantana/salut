package br.com.salut.salutbackend.dto;

import br.com.salut.salutbackend.model.Visita;
import java.math.BigDecimal;

// Usamos record para um DTO simples
public record DashboardDTO(
        Long pedidosPendentes,
        BigDecimal percentualMetaMes,
        BigDecimal valorComissaoTotal,
        Visita proximaVisita
) {
}