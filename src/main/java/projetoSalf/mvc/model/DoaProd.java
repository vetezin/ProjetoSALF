package projetoSalf.mvc.model;

public class DoaProd {
    private int doaProdCod;
    private int doaProdQtd;
    private int doaProdCatCod;
    private int produtoProdCod;
    private int doacaoDoaCod;

    public DoaProd() {
    }

    public DoaProd(int doaProdCod, int doaProdQtd, int doaProdCatCod, int produtoProdCod, int doacaoDoaCod) {
        this.doaProdCod = doaProdCod;
        this.doaProdQtd = doaProdQtd;
        this.doaProdCatCod = doaProdCatCod;
        this.produtoProdCod = produtoProdCod;
        this.doacaoDoaCod = doacaoDoaCod;
    }

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

