package projetoSalf.mvc.model;

public class ProdutoCompra {
    private int lcCod;
    private int prodCod;
    private int cotCod;
    private int fornCod;
    private int compraCod;
    private int qtd;
    private float valorCompra;

    public ProdutoCompra() {}

    public ProdutoCompra(int lcCod, int prodCod, int cotCod, int fornCod, int compraCod, int qtd, float valorCompra) {
        this.lcCod = lcCod;
        this.prodCod = prodCod;
        this.cotCod = cotCod;
        this.fornCod = fornCod;
        this.compraCod = compraCod;
        this.qtd = qtd;
        this.valorCompra = valorCompra;
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

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public float getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(float valorCompra) {
        this.valorCompra = valorCompra;
    }
}
