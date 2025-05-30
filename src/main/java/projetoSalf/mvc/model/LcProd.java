package projetoSalf.mvc.model;

public class LcProd {
    private int lcProdQtd;
    private int lcCod;
    private int prodCod;

    public LcProd() {
    }

    public LcProd(int lcProdQtd, int lcCod, int prodCod) {
        this.lcProdQtd = lcProdQtd;
        this.lcCod = lcCod;
        this.prodCod = prodCod;
    }

    public int getLcProdQtd() {
        return lcProdQtd;
    }

    public void setLcProdQtd(int lcProdQtd) {
        this.lcProdQtd = lcProdQtd;
    }

    public int getLcCod() {
        return lcCod;
    }

    public void setLcCod(int lcCod) {
        this.lcCod = lcCod;
    }

    public int getProdCod() {
        return prodCod;
    }

    public void setProdCod(int prodCod) {
        this.prodCod = prodCod;
    }
}
