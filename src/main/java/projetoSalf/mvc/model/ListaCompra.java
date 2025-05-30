package projetoSalf.mvc.model;

import java.time.LocalDate;

public class ListaCompra {
    private int lcCod;
    private LocalDate lcDtlista;
    private String lcDesc;
    private int funcCod;

    public ListaCompra() {
    }

    public ListaCompra(int lcCod, LocalDate lcDtlista, String lcDesc, int funcCod) {
        this.lcCod = lcCod;
        this.lcDtlista = lcDtlista;
        this.lcDesc = lcDesc;
        this.funcCod = funcCod;
    }

    public int getLcCod() {
        return lcCod;
    }

    public void setLcCod(int lcCod) {
        this.lcCod = lcCod;
    }

    public LocalDate getLcDtlista() {
        return lcDtlista;
    }

    public void setLcDtlista(LocalDate lcDtlista) {
        this.lcDtlista = lcDtlista;
    }

    public String getLcDesc() {
        return lcDesc;
    }

    public void setLcDesc(String lcDesc) {
        this.lcDesc = lcDesc;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}
