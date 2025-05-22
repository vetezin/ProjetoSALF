package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ListaCompraProdDAO;
import projetoSalf.mvc.util.Conexao;


import java.util.List;

@Component
public class ListaCompraProd {

    @Autowired
    private ListaCompraProdDAO dao;

    private int lc_cod;
    private int prod_cod;
    private int lc_prod_qtd;

    public ListaCompraProd() {}

    public ListaCompraProd(int lc_cod, int prod_cod, int lc_prod_qtd) {
        this.lc_cod = lc_cod;
        this.prod_cod = prod_cod;
        this.lc_prod_qtd = lc_prod_qtd;
    }

    // Getters e Setters
    public int getLc_cod() {
        return lc_cod;
    }

    public void setLc_cod(int lc_cod) {
        this.lc_cod = lc_cod;
    }

    public int getProd_cod() {
        return prod_cod;
    }

    public void setProd_cod(int prod_cod) {
        this.prod_cod = prod_cod;
    }

    public int getLc_prod_qtd() {
        return lc_prod_qtd;
    }

    public void setLc_prod_qtd(int lc_prod_qtd) {
        this.lc_prod_qtd = lc_prod_qtd;
    }

    // Métodos DAO padrão
    public ListaCompraProd gravar(ListaCompraProd item) {
        return dao.gravar(item);
    }

    public boolean apagar(ListaCompraProd item) {
        return dao.apagar(item);
    }

    public ListaCompraProd alterar(ListaCompraProd item) {
        return dao.alterar(item);
    }

    public List<ListaCompraProd> consultarPorLista(int lc_cod) {
        return dao.getPorListaCompra(lc_cod);
    }

    public boolean isEmpty() {
        return dao.getTodos().isEmpty();
    }

    public ListaCompraProd consultar(String s, Conexao conexao) {
        return this.dao.get(s);
    }

    public boolean deletar(ListaCompraProd existente) {
        return dao.apagar(existente);
    }

    public List<ListaCompraProd> getAll(String s, Conexao conexao) {
        return this.dao.get(s);
    }
}
