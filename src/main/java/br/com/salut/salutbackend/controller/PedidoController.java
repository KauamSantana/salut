package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.PedidoCadastroDTO;
import br.com.salut.salutbackend.model.*;
import br.com.salut.salutbackend.repository.ClienteRepository;
import br.com.salut.salutbackend.repository.PedidoRepository;
import br.com.salut.salutbackend.repository.RepresentanteRepository;
import br.com.salut.salutbackend.repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private VinhoRepository vinhoRepository;
    @Autowired private RepresentanteRepository representanteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoCadastroDTO data) {
        Cliente cliente = clienteRepository.findById(data.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Representante representante = representanteRepository.findById(data.representanteId())
                .orElseThrow(() -> new RuntimeException("Representante não encontrado"));

        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente);
        novoPedido.setRepresentante(representante);
        novoPedido.setDataDoPedido(LocalDateTime.now());
        novoPedido.setCondicoesDePagamento(data.condicoesDePagamento());
        novoPedido.setStatus("PENDENTE");

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

        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);
        return ResponseEntity.ok(pedidoSalvo);
    }

    @GetMapping
    public List<Pedido> listarPedidos(@RequestParam Optional<Long> clienteId, @RequestParam Optional<String> status) {
        if (clienteId.isPresent()) {
            return pedidoRepository.findByClienteId(clienteId.get());
        }
        if (status.isPresent()) {
            return pedidoRepository.findByStatus(status.get());
        }
        return pedidoRepository.findAll();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody Pedido detalhesPedido) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (detalhesPedido.getStatus() != null) {
                        pedido.setStatus(detalhesPedido.getStatus());
                    }
                    if (detalhesPedido.getCondicoesDePagamento() != null) {
                        pedido.setCondicoesDePagamento(detalhesPedido.getCondicoesDePagamento());
                    }
                    Pedido pedidoAtualizado = pedidoRepository.save(pedido);
                    return ResponseEntity.ok(pedidoAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }
}