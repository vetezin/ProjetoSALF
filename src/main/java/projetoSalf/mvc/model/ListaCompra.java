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
    private Date lc_data;
    private Funcionario lc_func;
    private List<Itens> itens;

    public ListaCompra(ListaCompraDAO dao, int lc_cod, Date lc_data, Funcionario lc_func, List<Itens> itens) {
        this.dao = dao;
        this.lc_cod = lc_cod;
        this.lc_data = lc_data;
        this.lc_func = lc_func;
        this.itens = itens;
    }

    public ListaCompra() {
    }

    public ListaCompraDAO getDao() {
        return dao;
    }

    public void setDao(ListaCompraDAO dao) {
        this.dao = dao;
    }

    public CharSequence getLc_cod() {
        return lc_cod;
    }

    public void setLc_cod(int lc_cod) {
        this.lc_cod = lc_cod;
    }

    public CharSequence getLc_data() {
        return lc_data;
    }

    public void setLc_data(Date lc_data) {
        this.lc_data = lc_data;
    }

    public Funcionario getLc_func() {
        return lc_func;
    }

    public void setLc_func(Funcionario lc_func) {
        this.lc_func = lc_func;
    }

    public List<Itens> getItens() {
        return itens;
    }

    public void setItens(List<Itens> itens) {
        this.itens = itens;
    }
    public List<ListaCompra> consultar(String filtro, Conexao conexao){
        return dao.get(filtro);
    }
    public ListaCompra consultar(int id){
        return dao.get(id);
    }
    public boolean isEmpty(){
        return dao.get("").isEmpty();
    }
    public ListaCompra gravar(ListaCompra lc) {
        return this.dao.gravar(lc);
    }
    public boolean deletarListaCompra(ListaCompra lc){
        return dao.apagar(lc);
    }

    public CharSequence getFuncCod() {
        return lc_func.getId();
    }
}
