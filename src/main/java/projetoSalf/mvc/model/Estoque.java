package projetoSalf.mvc.model;

import java.time.LocalDate;

public class Estoque {
    private String esQtdprod;
    private int estoqueId;
    private int produtoProdCod;
    private LocalDate esDtvalidade;

    public Estoque() {
    }

    public Estoque(String esQtdprod, int estoqueId, int produtoProdCod, LocalDate esDtvalidade) {
        this.esQtdprod = esQtdprod;
        this.estoqueId = estoqueId;
        this.produtoProdCod = produtoProdCod;
        this.esDtvalidade = esDtvalidade;
    }

    public String getEsQtdprod() {
        return esQtdprod;
    }

    public void setEsQtdprod(String esQtdprod) {
        this.esQtdprod = esQtdprod;
    }

    public int getEstoqueId() {
        return estoqueId;
    }

    public void setEstoqueId(int estoqueId) {
        this.estoqueId = estoqueId;
    }

    public int getProdutoProdCod() {
        return produtoProdCod;
    }

    public void setProdutoProdCod(int produtoProdCod) {
        this.produtoProdCod = produtoProdCod;
    }

    public LocalDate getEsDtvalidade() {
        return esDtvalidade;
    }

    public void setEsDtvalidade(LocalDate esDtvalidade) {
        this.esDtvalidade = esDtvalidade;
    }
}

