package br.com.salut.salutbackend.controller;

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

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VinhoRepository vinhoRepository;
    @Autowired
    private RepresentanteRepository representanteRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        pedido.setCliente(cliente);

        if (pedido.getRepresentante() != null && pedido.getRepresentante().getId() != null) {
            Representante representante = representanteRepository.findById(pedido.getRepresentante().getId())
                    .orElseThrow(() -> new RuntimeException("Representante não encontrado"));
            pedido.setRepresentante(representante);
        }

        pedido.setDataDoPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        // LÓGICA CORRIGIDA:
        // 1. Processa os itens primeiro
        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            pedido.getItens().forEach(item -> {
                Vinho vinho = vinhoRepository.findById(item.getVinho().getId())
                        .orElseThrow(() -> new RuntimeException("Vinho não encontrado"));
                item.setVinho(vinho);
                item.setPrecoUnitario(vinho.getPrecoUnitario() != null ? vinho.getPrecoUnitario() : BigDecimal.ZERO);
                item.setPedido(pedido);
            });
        }

        // 2. Agora, calcula o total a partir dos itens já processados
        BigDecimal totalDoPedido = BigDecimal.ZERO;
        if (pedido.getItens() != null) {
            totalDoPedido = pedido.getItens().stream()
                    .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        pedido.setValorTotal(totalDoPedido);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoSalvo);
    }

    @GetMapping
    public List<Pedido> listarPedidos(@RequestParam Optional<Long> clienteId, @RequestParam Optional<String> status) {
        if (clienteId.isPresent() && status.isPresent()) {
            // Esta é uma lógica que podemos adicionar depois para combinar filtros
            return pedidoRepository.findByClienteId(clienteId.get()); // Simplificado por agora
        }
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