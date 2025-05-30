package projetoSalf.mvc.model;

import java.time.LocalDate;

public class Saidas {
    private int sCod;
    private LocalDate sDtsaida;
    private String sMotivo;
    private int funcCod;

    public Saidas() {
    }

    public Saidas(int sCod, LocalDate sDtsaida, String sMotivo, int funcCod) {
        this.sCod = sCod;
        this.sDtsaida = sDtsaida;
        this.sMotivo = sMotivo;
        this.funcCod = funcCod;
    }

    public int getSCod() {
        return sCod;
    }

    public void setSCod(int sCod) {
        this.sCod = sCod;
    }

    public LocalDate getSDtsaida() {
        return sDtsaida;
    }

    public void setSDtsaida(LocalDate sDtsaida) {
        this.sDtsaida = sDtsaida;
    }

    public String getSMotivo() {
        return sMotivo;
    }

    public void setSMotivo(String sMotivo) {
        this.sMotivo = sMotivo;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}

