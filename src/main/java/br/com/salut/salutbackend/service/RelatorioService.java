package br.com.salut.salutbackend.service;

import br.com.salut.salutbackend.dto.VendasPorRegiaoDTO;
import br.com.salut.salutbackend.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<VendasPorRegiaoDTO> getVendasPorRegiao() {
        return pedidoRepository.findVendasPorRegiao();
    }
}