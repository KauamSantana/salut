package br.com.salut.salutbackend.controller;

import br.com.salut.salutbackend.dto.PedidoCadastroDTO;
import br.com.salut.salutbackend.dto.PedidoUpdateDTO;
import br.com.salut.salutbackend.model.Pedido;
import br.com.salut.salutbackend.repository.PedidoRepository;
import br.com.salut.salutbackend.service.PedidoService; // NOVO IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // AGORA INJETAMOS O SERVIÇO
    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoCadastroDTO data) {
        // A ÚNICA FUNÇÃO DO CONTROLLER AGORA É CHAMAR O SERVIÇO
        Pedido pedidoSalvo = pedidoService.criarPedido(data);
        return ResponseEntity.ok(pedidoSalvo);
    }

    @GetMapping
    public Page<Pedido> listarPedidos(
            @RequestParam Optional<Long> clienteId,
            @RequestParam Optional<String> status,
            Pageable pageable) {

        if (clienteId.isPresent()) {
            return pedidoRepository.findByClienteId(clienteId.get(), pageable);
        }
        if (status.isPresent()) {
            return pedidoRepository.findByStatus(status.get(), pageable);
        }
        return pedidoRepository.findAll(pageable);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody PedidoUpdateDTO detalhesPedido) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (detalhesPedido.status() != null) {
                        pedido.setStatus(detalhesPedido.status());
                    }
                    if (detalhesPedido.condicoesDePagamento() != null) {
                        pedido.setCondicoesDePagamento(detalhesPedido.condicoesDePagamento());
                    }
                    Pedido pedidoAtualizado = pedidoRepository.save(pedido);
                    return ResponseEntity.ok(pedidoAtualizado);
                }).orElse(ResponseEntity.notFound().build());
    }
}