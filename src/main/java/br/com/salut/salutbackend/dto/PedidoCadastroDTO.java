package br.com.salut.salutbackend.dto;

import java.util.List;

// DTO para receber dados para criar um pedido
public record PedidoCadastroDTO(
        Long clienteId,
        Long representanteId,
        String condicoesDePagamento,
        List<ItemPedidoDTO> itens
) {
}