package br.com.salut.salutbackend.dto;

// DTO para representar um item dentro de um pedido
public record ItemPedidoDTO(Long vinhoId, int quantidade) {
}