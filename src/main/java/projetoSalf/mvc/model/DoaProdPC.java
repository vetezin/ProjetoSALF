package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.DoaProdPCDAO;

@Component
public class DoaProdPC {

    @Autowired
    private DoaProdPCDAO dao;

    private int doaProdQtd;
    private int doaProdCatCod;
    private int produtoProdCod;
    private int doapcCod; // código da doação para pessoa carente

    // Getters e Setters
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

    public int getDoapcCod() {
        return doapcCod;
    }

    public void setDoapcCod(int doapcCod) {
        this.doapcCod = doapcCod;
    }

    // Métodos de persistência
    public boolean gravar() {
        return dao.gravar(toDoaProd(), doapcCod);
    }

    // Converte para a estrutura já usada no DAO
    private DoaProd toDoaProd() {
        DoaProd dp = new DoaProd();
        dp.setDoaProdQtd(this.doaProdQtd);
        dp.setDoaProdCatCod(this.doaProdCatCod);
        dp.setProdutoProdCod(this.produtoProdCod);
        return dp;
    }
}
