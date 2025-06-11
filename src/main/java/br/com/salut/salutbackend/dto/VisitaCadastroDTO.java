package br.com.salut.salutbackend.dto;

import java.time.LocalDateTime;

// DTO para receber dados para criar/atualizar uma visita
public record VisitaCadastroDTO(
        Long clienteId,
        Long representanteId,
        LocalDateTime dataHora,
        String observacoes,
        String status
) {
}