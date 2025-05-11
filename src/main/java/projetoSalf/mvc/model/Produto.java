package projetoSalf.mvc.model;
import java.time.LocalDate;

public class Produto {
    private int prodCod;
    private LocalDate prodDtvalid;
    private String prodDesc;
    private float prodValorun;
    private int catCod;

    public Produto() {
    }

    public Produto(int prodCod, LocalDate prodDtvalid, String prodDesc, float prodValorun, int catCod) {
        this.prodCod = prodCod;
        this.prodDtvalid = prodDtvalid;
        this.prodDesc = prodDesc;
        this.prodValorun = prodValorun;
        this.catCod = catCod;
    }

    public int getProdCod() {
        return prodCod;
    }

    public void setProdCod(int prodCod) {
        this.prodCod = prodCod;
    }

    public LocalDate getProdDtvalid() {
        return prodDtvalid;
    }

    public void setProdDtvalid(LocalDate prodDtvalid) {
        this.prodDtvalid = prodDtvalid;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public float getProdValorun() {
        return prodValorun;
    }

    public void setProdValorun(float prodValorun) {
        this.prodValorun = prodValorun;
    }

    public int getCatCod() {
        return catCod;
    }

    public void setCatCod(int catCod) {
        this.catCod = catCod;
    }
}

