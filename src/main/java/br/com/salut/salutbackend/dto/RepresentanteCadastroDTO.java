package br.com.salut.salutbackend.dto;

import java.math.BigDecimal;

// Usamos um record para um DTO simples e imutável
public record RepresentanteCadastroDTO(
        String nome,
        String sobrenome,
        String email,
        String telefone,
        String regiao,
        String status,
        String senha,
        BigDecimal meta,
        BigDecimal taxaComissao,
        String role
) {
}