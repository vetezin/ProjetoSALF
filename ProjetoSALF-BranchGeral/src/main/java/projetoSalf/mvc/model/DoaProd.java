package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.DoaProdDAO;

public class DoaProd {
    private int doaProdCod;
    private int doaProdQtd;
    private int doaProdCatCod;
    private int produtoProdCod;
    private int doacaoDoaCod;

    public DoaProd() {}

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
}

