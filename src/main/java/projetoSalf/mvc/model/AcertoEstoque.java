package projetoSalf.mvc.model;

import java.time.LocalDate;

public class AcertoEstoque {
    private int aeCod;
    private LocalDate aeData;
    private String aeMotivo;
    private float aeQtd;
    private int prodCod;
    private int funcCod;

    public AcertoEstoque() {
    }

    public AcertoEstoque(int aeCod, LocalDate aeData, String aeMotivo, float aeQtd, int prodCod, int funcCod) {
        this.aeCod = aeCod;
        this.aeData = aeData;
        this.aeMotivo = aeMotivo;
        this.aeQtd = aeQtd;
        this.prodCod = prodCod;
        this.funcCod = funcCod;
    }

    public int getAeCod() {
        return aeCod;
    }

    public void setAeCod(int aeCod) {
        this.aeCod = aeCod;
    }

    public LocalDate getAeData() {
        return aeData;
    }

    public void setAeData(LocalDate aeData) {
        this.aeData = aeData;
    }

    public String getAeMotivo() {
        return aeMotivo;
    }

    public void setAeMotivo(String aeMotivo) {
        this.aeMotivo = aeMotivo;
    }

    public float getAeQtd() {
        return aeQtd;
    }

    public void setAeQtd(float aeQtd) {
        this.aeQtd = aeQtd;
    }

    public int getProdCod() {
        return prodCod;
    }

    public void setProdCod(int prodCod) {
        this.prodCod = prodCod;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}

