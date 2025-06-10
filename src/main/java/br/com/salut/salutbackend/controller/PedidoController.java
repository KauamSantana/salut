package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.model.Cliente;
import br.com.salut.salutbackend.model.ItemPedido;
import br.com.salut.salutbackend.model.Pedido;
import br.com.salut.salutbackend.model.Vinho;
import br.com.salut.salutbackend.repository.ClienteRepository;
import br.com.salut.salutbackend.repository.PedidoRepository;
import br.com.salut.salutbackend.repository.VinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VinhoRepository vinhoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        pedido.setCliente(cliente);
        pedido.setDataDoPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            List<ItemPedido> itensProcessados = pedido.getItens().stream().map(item -> {
                Vinho vinho = vinhoRepository.findById(item.getVinho().getId())
                        .orElseThrow(() -> new RuntimeException("Vinho não encontrado"));
                item.setVinho(vinho);
                if (vinho.getPrecoUnitario() != null) {
                    item.setPrecoUnitario(vinho.getPrecoUnitario());
                }
                item.setPedido(pedido);
                return item;
            }).collect(Collectors.toList());
            pedido.setItens(itensProcessados);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoSalvo);
    }

    @GetMapping
    public List<Pedido> listarPedidos(
            @RequestParam Optional<Long> clienteId,
            @RequestParam Optional<String> status) {

        if (clienteId.isPresent()) {
            return pedidoRepository.findByClienteId(clienteId.get());
        }
        if (status.isPresent()) {
            return pedidoRepository.findByStatus(status.get());
        }
        return pedidoRepository.findAll();
    }

    // NOVO MÉTODO PARA ATUALIZAR UM PEDIDO
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody Pedido detalhesPedido) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    // Apenas o status e as condições de pagamento podem ser atualizados por este endpoint
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