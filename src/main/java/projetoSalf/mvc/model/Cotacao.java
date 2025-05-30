package projetoSalf.mvc.model;

import java.time.LocalDate;

public class Cotacao {
    private int cotCod;
    private LocalDate cotDtabertura;
    private LocalDate cotDtfechamento;
    private int lcCod;

    public Cotacao() {
    }

    public Cotacao(int cotCod, LocalDate cotDtabertura, LocalDate cotDtfechamento, int lcCod) {
        this.cotCod = cotCod;
        this.cotDtabertura = cotDtabertura;
        this.cotDtfechamento = cotDtfechamento;
        this.lcCod = lcCod;
    }

    public int getCotCod() {
        return cotCod;
    }

    public void setCotCod(int cotCod) {
        this.cotCod = cotCod;
    }

    public LocalDate getCotDtabertura() {
        return cotDtabertura;
    }

    public void setCotDtabertura(LocalDate cotDtabertura) {
        this.cotDtabertura = cotDtabertura;
    }

    public LocalDate getCotDtfechamento() {
        return cotDtfechamento;
    }

    public void setCotDtfechamento(LocalDate cotDtfechamento) {
        this.cotDtfechamento = cotDtfechamento;
    }

    public int getLcCod() {
        return lcCod;
    }

    public void setLcCod(int lcCod) {
        this.lcCod = lcCod;
    }
}

