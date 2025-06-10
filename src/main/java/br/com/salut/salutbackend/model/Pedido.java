package br.com.salut.salutbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "representante_id")
    private Representante representante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    private LocalDateTime dataDoPedido;
    private String condicoesDePagamento;
    private String status;

    // Getters e Setters manuais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Representante getRepresentante() { return representante; }
    public void setRepresentante(Representante representante) { this.representante = representante; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public LocalDateTime getDataDoPedido() { return dataDoPedido; }
    public void setDataDoPedido(LocalDateTime dataDoPedido) { this.dataDoPedido = dataDoPedido; }
    public String getCondicoesDePagamento() { return condicoesDePagamento; }
    public void setCondicoesDePagamento(String condicoesDePagamento) { this.condicoesDePagamento = condicoesDePagamento; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}