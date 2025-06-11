package projetoSalf.mvc.model;

import java.time.LocalDate;

public class CotForn {
    private int cotCod;
    private int fornCod;
    private LocalDate cotFornDtCotacao;

    public CotForn() {
    }

    public CotForn(int cotCod, int fornCod, LocalDate cotFornDtCotacao) {
        this.cotCod = cotCod;
        this.fornCod = fornCod;
        this.cotFornDtCotacao = cotFornDtCotacao;
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

    public LocalDate getCotFornDtCotacao() {
        return cotFornDtCotacao;
    }

    public void setCotFornDtCotacao(LocalDate cotFornDtCotacao) {
        this.cotFornDtCotacao = cotFornDtCotacao;
    }
}

