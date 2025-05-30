package projetoSalf.mvc.model;

public class CbProd {
    private float prodQtd;
    private int cbProdId;
    private int produtoProdCod;
    private int cestaBasicaCbCod;

    public CbProd() {
    }

    public CbProd(float prodQtd, int cbProdId, int produtoProdCod, int cestaBasicaCbCod) {
        this.prodQtd = prodQtd;
        this.cbProdId = cbProdId;
        this.produtoProdCod = produtoProdCod;
        this.cestaBasicaCbCod = cestaBasicaCbCod;
    }

    public float getProdQtd() {
        return prodQtd;
    }

    public void setProdQtd(float prodQtd) {
        this.prodQtd = prodQtd;
    }

    public int getCbProdId() {
        return cbProdId;
    }

    public void setCbProdId(int cbProdId) {
        this.cbProdId = cbProdId;
    }

    public int getProdutoProdCod() {
        return produtoProdCod;
    }

    public void setProdutoProdCod(int produtoProdCod) {
        this.produtoProdCod = produtoProdCod;
    }

    public int getCestaBasicaCbCod() {
        return cestaBasicaCbCod;
    }

    public void setCestaBasicaCbCod(int cestaBasicaCbCod) {
        this.cestaBasicaCbCod = cestaBasicaCbCod;
    }
}

