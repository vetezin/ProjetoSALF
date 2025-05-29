package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.EstoqueDAO;

import java.util.List;

@Component
public class Estoque {

    @Autowired
    private EstoqueDAO dao;

    private int estoque_id;
    private int es_qtdprod; // agora é int
    private String es_dtvalidade;
    private int produto_prod_cod;

    public Estoque() {}

    public Estoque(int estoque_id, int es_qtdprod, String es_dtvalidade, int produto_prod_cod) {
        this.estoque_id = estoque_id;
        this.es_qtdprod = es_qtdprod;
        this.es_dtvalidade = es_dtvalidade;
        this.produto_prod_cod = produto_prod_cod;
    }

    public Estoque(int es_qtdprod, String es_dtvalidade, int produto_prod_cod) {
        this.es_qtdprod = es_qtdprod;
        this.es_dtvalidade = es_dtvalidade;
        this.produto_prod_cod = produto_prod_cod;
    }

    public int getEstoque_id() {
        return estoque_id;
    }

    public void setEstoque_id(int estoque_id) {
        this.estoque_id = estoque_id;
    }

    public int getEs_qtdprod() {
        return es_qtdprod;
    }

    public void setEs_qtdprod(int es_qtdprod) {
        this.es_qtdprod = es_qtdprod;
    }

    public String getEs_dtvalidade() {
        return es_dtvalidade;
    }

    public void setEs_dtvalidade(String es_dtvalidade) {
        this.es_dtvalidade = es_dtvalidade;
    }

    public int getProduto_prod_cod() {
        return produto_prod_cod;
    }

    public void setProduto_prod_cod(int produto_prod_cod) {
        this.produto_prod_cod = produto_prod_cod;
    }

    //  DAO

    public List<Estoque> consultar(String filtro) {
        return dao.get(filtro);
    }

    public Estoque consultarPorId(int id) {
        return dao.get(id);
    }

    public Estoque gravar(Estoque estoque) {
        return dao.gravar(estoque);
    }

    public Estoque alterar(Estoque estoque) {
        return dao.alterar(estoque);
    }

    public boolean deletar(Estoque estoque) {
        return dao.apagar(estoque);
    }

    public boolean isEmpty() {
        return dao.get("").isEmpty();
    }
}
