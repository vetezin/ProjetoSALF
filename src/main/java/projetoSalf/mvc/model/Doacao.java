package projetoSalf.mvc.model;

import java.time.LocalDate;

public class Doacao {
    private int doaCod;
    private LocalDate doaDtentrada;
    private int funcCod;

    public Doacao() {
    }

    public Doacao(int doaCod, LocalDate doaDtentrada, int funcCod) {
        this.doaCod = doaCod;
        this.doaDtentrada = doaDtentrada;
        this.funcCod = funcCod;
    }

    public int getDoaCod() {
        return doaCod;
    }

    public void setDoaCod(int doaCod) {
        this.doaCod = doaCod;
    }

    public LocalDate getDoaDtentrada() {
        return doaDtentrada;
    }

    public void setDoaDtentrada(LocalDate doaDtentrada) {
        this.doaDtentrada = doaDtentrada;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}

