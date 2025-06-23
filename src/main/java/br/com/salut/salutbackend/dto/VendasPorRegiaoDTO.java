package br.com.salut.salutbackend.dto;

import java.math.BigDecimal;

public class VendasPorRegiaoDTO {
    private String regiao;
    private BigDecimal totalVendas;

    // Construtor que o seu PedidoRepository usa
    public VendasPorRegiaoDTO(String regiao, BigDecimal totalVendas) {
        this.regiao = regiao;
        this.totalVendas = totalVendas;
    }

    // Getters e Setters
    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public BigDecimal getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(BigDecimal totalVendas) {
        this.totalVendas = totalVendas;
    }
}