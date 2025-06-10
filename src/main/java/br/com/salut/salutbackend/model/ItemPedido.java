package br.com.salut.salutbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidade;
    private BigDecimal precoUnitario; // Usamos BigDecimal para valores monetários

    @ManyToOne
    @JoinColumn(name = "vinho_id")
    private Vinho vinho;

    @ManyToOne
    @JsonIgnore // Ignora este campo na resposta JSON para evitar um loop infinito
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    // Getters e Setters manuais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public Vinho getVinho() { return vinho; }
    public void setVinho(Vinho vinho) { this.vinho = vinho; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
}