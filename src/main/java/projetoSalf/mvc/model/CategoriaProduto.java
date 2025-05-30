package projetoSalf.mvc.model;

public class CategoriaProduto {
    private int catCod;
    private String catDesc;

    public CategoriaProduto() {
    }

    public CategoriaProduto(int catCod, String catDesc) {
        this.catCod = catCod;
        this.catDesc = catDesc;
    }

    public int getCatCod() {
        return catCod;
    }

    public void setCatCod(int catCod) {
        this.catCod = catCod;
    }

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }
}

