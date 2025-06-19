package br.com.salut.salutbackend.service;

import br.com.salut.salutbackend.dto.PedidoCadastroDTO;
import br.com.salut.salutbackend.model.*;
import br.com.salut.salutbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private RepresentanteRepository representanteRepository;
    @Autowired private VinhoRepository vinhoRepository;

    // Dentro da classe PedidoService

    @Transactional
    public Pedido criarPedido(PedidoCadastroDTO data) {
        Cliente cliente = clienteRepository.findById(data.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + data.clienteId() + " não encontrado"));

        Representante representante = representanteRepository.findById(data.representanteId())
                .orElseThrow(() -> new RuntimeException("Representante com ID " + data.representanteId() + " não encontrado"));

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente);
        novoPedido.setRepresentante(representante); // Garantindo que o representante está associado
        novoPedido.setDataDoPedido(LocalDateTime.now());
        novoPedido.setCondicoesDePagamento(data.condicoesDePagamento());
        novoPedido.setStatus("PENDENTE");

        if (data.itens() == null || data.itens().isEmpty()) {
            throw new IllegalArgumentException("A lista de itens não pode ser vazia.");
        }

        List<ItemPedido> itens = data.itens().stream().map(itemDTO -> {
            Vinho vinho = vinhoRepository.findById(itemDTO.vinhoId())
                    .orElseThrow(() -> new RuntimeException("Vinho com ID " + itemDTO.vinhoId() + " não encontrado"));
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setVinho(vinho);
            itemPedido.setQuantidade(itemDTO.quantidade());
            itemPedido.setPrecoUnitario(vinho.getPrecoUnitario());
            itemPedido.setPedido(novoPedido);
            return itemPedido;
        }).collect(Collectors.toList());

        novoPedido.setItens(itens);

        BigDecimal totalDoPedido = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        novoPedido.setValorTotal(totalDoPedido);

        return pedidoRepository.save(novoPedido);
    }
}