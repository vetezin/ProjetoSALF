package projetoSalf.mvc.model;

public class SaidaProd {
    private int produtoProdCod;
    private int saidaSCod;
    private float spQtd;

    public SaidaProd() {
    }

    public SaidaProd(int produtoProdCod, int saidaSCod, float spQtd) {
        this.produtoProdCod = produtoProdCod;
        this.saidaSCod = saidaSCod;
        this.spQtd = spQtd;
    }

    public int getProdutoProdCod() {
        return produtoProdCod;
    }

    public void setProdutoProdCod(int produtoProdCod) {
        this.produtoProdCod = produtoProdCod;
    }

    public int getSaidaSCod() {
        return saidaSCod;
    }

    public void setSaidaSCod(int saidaSCod) {
        this.saidaSCod = saidaSCod;
    }

    public float getSpQtd() {
        return spQtd;
    }

    public void setSpQtd(float spQtd) {
        this.spQtd = spQtd;
    }
}
