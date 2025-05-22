package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ListaCompraDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.Date;
import java.util.List;

@Component
public class ListaCompra {

    @Autowired
    private ListaCompraDAO dao;

    private int lc_cod;
    private Date lc_dtlista;
    private String lc_desc;
    private int func_cod;
    private List<ListaCompraProd> itens;

    public ListaCompra() {}

    public ListaCompra(int lc_cod, Date lc_dtlista, String lc_desc, int func_cod) {
        this.lc_cod = lc_cod;
        this.lc_dtlista = lc_dtlista;
        this.lc_desc = lc_desc;
        this.func_cod = func_cod;
    }

    public ListaCompra(String desc, String dtlista, int funcCod) {
        this.lc_desc = desc;
        this.lc_dtlista = new Date(dtlista);
        this.func_cod = funcCod;
    }

    // Getters e Setters
    public int getLc_cod() {
        return lc_cod;
    }

    public void setLc_cod(int lc_cod) {
        this.lc_cod = lc_cod;
    }

    public Date getLc_dtlista() {
        return lc_dtlista;
    }

    public void setLc_dtlista(Date lc_dtlista) {
        this.lc_dtlista = lc_dtlista;
    }

    public String getLc_desc() {
        return lc_desc;
    }

    public void setLc_desc(String lc_desc) {
        this.lc_desc = lc_desc;
    }

    public int getFunc_cod() {
        return func_cod;
    }

    public void setFunc_cod(int func_cod) {
        this.func_cod = func_cod;
    }

    public List<ListaCompraProd> getItens() {
        return itens;
    }

    public void setItens(List<ListaCompraProd> itens) {
        this.itens = itens;
    }

    // Métodos usando DAO
    public ListaCompra gravar(ListaCompra lista) {
        return dao.gravar(lista);
    }

    public boolean deletar(ListaCompra lista) {
        return dao.apagar(lista);
    }

    public ListaCompra alterar(ListaCompra lista) {
        return dao.alterar(lista);
    }

    public ListaCompra consultar(int lc_cod) {
        return dao.get(lc_cod);
    }

    public List<ListaCompra> consultar(String filtro, Conexao conexao) {
        return dao.get(filtro);
    }

    public boolean isEmpty() {
        return dao.get("").isEmpty();
    }

    public ListaCompra update(ListaCompra existente) {
        return dao.alterar(existente);
    }
}
