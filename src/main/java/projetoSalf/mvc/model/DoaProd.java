package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.DoaProdDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

@Component
public class DoaProd {
    @Autowired
    private DoaProdDAO dao;

    private int doaProdCod;
    private int doaProdQtd;
    private int doaProdCatCod;
    private int produtoProdCod;
    private int doacaoDoaCod;

    // Getters e Setters
    public int getDoaProdCod() {
        return doaProdCod;
    }

    public void setDoaProdCod(int doaProdCod) {
        this.doaProdCod = doaProdCod;
    }

    public int getDoaProdQtd() {
        return doaProdQtd;
    }

    public void setDoaProdQtd(int doaProdQtd) {
        this.doaProdQtd = doaProdQtd;
    }

    public int getDoaProdCatCod() {
        return doaProdCatCod;
    }

    public void setDoaProdCatCod(int doaProdCatCod) {
        this.doaProdCatCod = doaProdCatCod;
    }

    public int getProdutoProdCod() {
        return produtoProdCod;
    }

    public void setProdutoProdCod(int produtoProdCod) {
        this.produtoProdCod = produtoProdCod;
    }

    public int getDoacaoDoaCod() {
        return doacaoDoaCod;
    }

    public void setDoacaoDoaCod(int doacaoDoaCod) {
        this.doacaoDoaCod = doacaoDoaCod;
    }

    // Métodos com DAO
    public boolean gravar() {
        return dao.gravar(this);
    }

    public List<DoaProd> listarPorDoacao(int doaCod) {
        return dao.listarPorDoacao(doaCod);
    }

    public boolean reverterDoacao(int doaCod) {
        return dao.reverterDoacao(doaCod);
    }

    public boolean reverterProdutosDaDoacaoPC(int doapcCod) {
        return dao.reverterProdutosDaDoacaoPC(doapcCod);
    }
}