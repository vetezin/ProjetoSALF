package projetoSalf.mvc.model;

public class ProdCotForn {
    private int lcCod;
    private int prodCod;
    private int cotCod;
    private int fornCod;
    private float pctfPreco;
    private float pctfQtd;

    public ProdCotForn() {
    }

    public ProdCotForn(int lcCod, int prodCod, int cotCod, int fornCod, float pctfPreco, float pctfQtd) {
        this.lcCod = lcCod;
        this.prodCod = prodCod;
        this.cotCod = cotCod;
        this.fornCod = fornCod;
        this.pctfPreco = pctfPreco;
        this.pctfQtd = pctfQtd;
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

    public int getCotCod() {
        return cotCod;
    }

    public void setCotCod(int cotCod) {
        this.cotCod = cotCod;
    }

    public int getFornCod() {
        return fornCod;
    }

    public void setFornCod(int fornCod) {
        this.fornCod = fornCod;
    }

    public float getPctfPreco() {
        return pctfPreco;
    }

    public void setPctfPreco(float pctfPreco) {
        this.pctfPreco = pctfPreco;
    }

    public float getPctfQtd() {
        return pctfQtd;
    }

    public void setPctfQtd(float pctfQtd) {
        this.pctfQtd = pctfQtd;
    }
}
