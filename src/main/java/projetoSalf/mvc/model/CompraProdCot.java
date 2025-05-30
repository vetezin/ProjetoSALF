package projetoSalf.mvc.model;

public class CompraProdCot {
    private int cpcQtd;
    private float cpcValorCompra;
    private int lcCod;
    private int prodCod;
    private int cotCod;
    private int fornCod;
    private int compraCod;

    public CompraProdCot() {
    }

    public CompraProdCot(int cpcQtd, float cpcValorCompra, int lcCod, int prodCod, int cotCod,
                         int fornCod, int compraCod) {
        this.cpcQtd = cpcQtd;
        this.cpcValorCompra = cpcValorCompra;
        this.lcCod = lcCod;
        this.prodCod = prodCod;
        this.cotCod = cotCod;
        this.fornCod = fornCod;
        this.compraCod = compraCod;
    }

    public int getCpcQtd() {
        return cpcQtd;
    }

    public void setCpcQtd(int cpcQtd) {
        this.cpcQtd = cpcQtd;
    }

    public float getCpcValorCompra() {
        return cpcValorCompra;
    }

    public void setCpcValorCompra(float cpcValorCompra) {
        this.cpcValorCompra = cpcValorCompra;
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

    public int getCompraCod() {
        return compraCod;
    }

    public void setCompraCod(int compraCod) {
        this.compraCod = compraCod;
    }
}
