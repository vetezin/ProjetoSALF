package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ItensDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

@Component
public class Itens {

    @Autowired
    private ItensDAO dao;

    private int listaId;
    private int produtoId;
    private int quantidade;

    public Itens() {}

    public Itens(int listaId, int produtoId, int quantidade) {
        this.listaId = listaId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public int getListaId() {
        return listaId;
    }

    public void setListaId(int listaId) {
        this.listaId = listaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // Métodos com DAO
    public Itens gravar(Itens novo) {
        return dao.gravar(novo);
    }

    public List<Itens> consultarPorLista(int listaId, Conexao conexao) {
        return dao.getItensPorLista(listaId);
    }

    public Itens consultar(int listaId, int produtoId) {
        return dao.consultar(listaId, produtoId);
    }

    public Itens update(Itens atualizado) {
        return dao.update(atualizado);
    }

    public boolean deletarListaCompraProd(Itens item) {
        return dao.deletar(item);
    }
}