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
        // Busca o cliente do banco para garantir que ele existe
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Prepara o pedido
        pedido.setCliente(cliente);
        pedido.setDataDoPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        // Processa os itens do pedido, se existirem
        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            List<ItemPedido> itensProcessados = pedido.getItens().stream().map(item -> {
                Vinho vinho = vinhoRepository.findById(item.getVinho().getId())
                        .orElseThrow(() -> new RuntimeException("Vinho não encontrado"));
                item.setVinho(vinho);
                // Supondo que Vinho tenha um preço. Se não, esta linha pode ser removida ou ajustada.
                if (vinho.getPrecoUnitario() != null) {
                    item.setPrecoUnitario(vinho.getPrecoUnitario());
                }
                item.setPedido(pedido); // Associa o item ao pedido principal
                return item;
            }).collect(Collectors.toList());
            pedido.setItens(itensProcessados);
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoSalvo);
    }

    @GetMapping
    public List<Pedido> listarPedidos(@RequestParam Optional<Long> clienteId) {
        if (clienteId.isPresent()) {
            return pedidoRepository.findByClienteId(clienteId.get());
        }
        return pedidoRepository.findAll();
    }
}