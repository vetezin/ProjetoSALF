package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ItensDAO;

@Component
public class Itens {
    @Autowired
    private ItensDAO dao;

    private int itens_cod;
    private int lc_cod;
    private Produto produto;
    private int qtd;

    public Itens() {
    }

    public Itens(ItensDAO dao, int itens_cod, int lc_cod, Produto produto, int qtd) {
        this.dao = dao;
        this.itens_cod = itens_cod;
        this.lc_cod = lc_cod;
        this.produto = produto;
        this.qtd = qtd;
    }

    public ItensDAO getDao() {
        return dao;
    }

    public void setDao(ItensDAO dao) {
        this.dao = dao;
    }

    public int getItens_cod() {
        return itens_cod;
    }

    public void setItens_cod(int itens_cod) {
        this.itens_cod = itens_cod;
    }

    public int getLc_cod() {
        return lc_cod;
    }

    public void setLc_cod(int lc_cod) {
        this.lc_cod = lc_cod;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public int getProdutoCod() {
        return produto.getId();
    }
}
