package br.com.github.jordihofc.springkafkasample.vendas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Venda {
    private UUID id= randomUUID();
    private String descricao;
    private BigDecimal preco;
    private LocalDate vendidoEm;

    public Venda(String descricao, BigDecimal preco, LocalDate vendidoEm) {
        this.descricao = descricao;
        this.preco = preco;
        this.vendidoEm = vendidoEm;
    }

    public Venda() {
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public LocalDate getVendidoEm() {
        return vendidoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(id, venda.id) && Objects.equals(descricao, venda.descricao) && Objects.equals(preco, venda.preco) && Objects.equals(vendidoEm, venda.vendidoEm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, preco, vendidoEm);
    }
}

