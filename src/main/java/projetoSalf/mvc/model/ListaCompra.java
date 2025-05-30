package projetoSalf.mvc.model;

<<<<<<< HEAD
import java.time.LocalDate;

public class ListaCompra {
    private int lcCod;
    private LocalDate lcDtlista;
    private String lcDesc;
    private int funcCod;

    public ListaCompra() {
    }

    public ListaCompra(int lcCod, LocalDate lcDtlista, String lcDesc, int funcCod) {
        this.lcCod = lcCod;
        this.lcDtlista = lcDtlista;
        this.lcDesc = lcDesc;
        this.funcCod = funcCod;
    }

    public int getLcCod() {
        return lcCod;
    }

    public void setLcCod(int lcCod) {
        this.lcCod = lcCod;
    }

    public LocalDate getLcDtlista() {
        return lcDtlista;
    }

    public void setLcDtlista(LocalDate lcDtlista) {
        this.lcDtlista = lcDtlista;
    }

    public String getLcDesc() {
        return lcDesc;
    }

    public void setLcDesc(String lcDesc) {
        this.lcDesc = lcDesc;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ListaCompraDAO;

import java.util.List;

@Component
public class ListaCompra {

    @Autowired
    private ListaCompraDAO dao;

    private int id;
    private String descricao;
    private String dataCriacao;
    private int funcionarioId;

    public ListaCompra() {}

    public ListaCompra(int id, String descricao, String dataCriacao, int funcionarioId) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra(String descricao, String dataCriacao, int funcionarioId) {
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

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra consultar(int id) {
        return dao.get(id);
    }

    public List<ListaCompra> consultar(String filtro) {
        return dao.get(filtro);
    }
    public ListaCompra gravar() {
        return dao.gravar(this);
    }

    public ListaCompra alterar() {
        return dao.alterar(this);
    }

    public boolean apagar(ListaCompra lista) {
        return dao.apagar(this);
    }

}
>>>>>>> Geral
