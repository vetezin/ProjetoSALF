package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ListaCompraDAO;

import java.time.LocalDate;
import java.util.List;

@Component
public class ListaCompra {

    @Autowired
    private ListaCompraDAO dao;

    private int id;
    private String descricao;
    private LocalDate dataCriacao;
    private int funcionarioId;

    public ListaCompra() {}

    public ListaCompra(int id, String descricao, LocalDate dataCriacao, int funcionarioId) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra(String descricao, LocalDate dataCriacao, int funcionarioId) {
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra gravar(ListaCompra nova) {
        return dao.gravar(this);
    }

    public ListaCompra alterar(ListaCompra existente) {
        return dao.alterar(this);
    }

    public boolean apagar(ListaCompra lista) {
        return dao.apagar(this);
    }

    public ListaCompra consultar(int id) {
        return dao.get(id);
    }

    public List<ListaCompra> consultar(String filtro) {
        return dao.get(filtro);
    }
}
