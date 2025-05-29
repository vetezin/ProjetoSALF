package projetoSalf.mvc.model;

import java.time.LocalDate;

public class Compra {
    private int coCod;
    private String coDescricao;
    private float coValorTotal;
    private LocalDate coDtCompra;
    private int coTotalItens;
    private int funcionarioFuncCod;
    private int cotFornCotacaoCotCod;
    private int cotFornFornecedorFornCod;

    public Compra() {
    }

    public Compra(int coCod, String coDescricao, float coValorTotal, LocalDate coDtCompra, int coTotalItens,
                  int funcionarioFuncCod, int cotFornCotacaoCotCod, int cotFornFornecedorFornCod) {
        this.coCod = coCod;
        this.coDescricao = coDescricao;
        this.coValorTotal = coValorTotal;
        this.coDtCompra = coDtCompra;
        this.coTotalItens = coTotalItens;
        this.funcionarioFuncCod = funcionarioFuncCod;
        this.cotFornCotacaoCotCod = cotFornCotacaoCotCod;
        this.cotFornFornecedorFornCod = cotFornFornecedorFornCod;
    }

    public int getCoCod() {
        return coCod;
    }

    public void setCoCod(int coCod) {
        this.coCod = coCod;
    }

    public String getCoDescricao() {
        return coDescricao;
    }

    public void setCoDescricao(String coDescricao) {
        this.coDescricao = coDescricao;
    }

    public float getCoValorTotal() {
        return coValorTotal;
    }

    public void setCoValorTotal(float coValorTotal) {
        this.coValorTotal = coValorTotal;
    }

    public LocalDate getCoDtCompra() {
        return coDtCompra;
    }

    public void setCoDtCompra(LocalDate coDtCompra) {
        this.coDtCompra = coDtCompra;
    }

    public int getCoTotalItens() {
        return coTotalItens;
    }

    public void setCoTotalItens(int coTotalItens) {
        this.coTotalItens = coTotalItens;
    }

    public int getFuncionarioFuncCod() {
        return funcionarioFuncCod;
    }

    public void setFuncionarioFuncCod(int funcionarioFuncCod) {
        this.funcionarioFuncCod = funcionarioFuncCod;
    }

    public int getCotFornCotacaoCotCod() {
        return cotFornCotacaoCotCod;
    }

    public void setCotFornCotacaoCotCod(int cotFornCotacaoCotCod) {
        this.cotFornCotacaoCotCod = cotFornCotacaoCotCod;
    }

    public int getCotFornFornecedorFornCod() {
        return cotFornFornecedorFornCod;
    }

    public void setCotFornFornecedorFornCod(int cotFornFornecedorFornCod) {
        this.cotFornFornecedorFornCod = cotFornFornecedorFornCod;
    }
}

