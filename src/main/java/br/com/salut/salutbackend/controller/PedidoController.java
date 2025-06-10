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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
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

        // Processa os itens do pedido
        List<ItemPedido> itensProcessados = pedido.getItens().stream().map(item -> {
            Vinho vinho = vinhoRepository.findById(item.getVinho().getId())
                    .orElseThrow(() -> new RuntimeException("Vinho não encontrado"));
            item.setVinho(vinho);
            item.setPrecoUnitario(vinho.getPrecoUnitario()); // Supondo que Vinho tenha um preço
            item.setPedido(pedido); // Associa o item ao pedido principal
            return item;
        }).collect(Collectors.toList());

        pedido.setItens(itensProcessados);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(pedidoSalvo);
    }
}