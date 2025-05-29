package projetoSalf.mvc.model;

import java.time.LocalDate;

public class CestaBasica {
    private int cbCod;
    private String cbMotivo;
    private LocalDate cbDtCriacao;
    private int pessoaCarentePcCod;
    private int cbCodFunc;
    private int funcionarioFuncCod;
    private LocalDate cbDtDoacao;

    public CestaBasica() {
    }

    public CestaBasica(int cbCod, String cbMotivo, LocalDate cbDtCriacao, int pessoaCarentePcCod, int cbCodFunc, int funcionarioFuncCod, LocalDate cbDtDoacao) {
        this.cbCod = cbCod;
        this.cbMotivo = cbMotivo;
        this.cbDtCriacao = cbDtCriacao;
        this.pessoaCarentePcCod = pessoaCarentePcCod;
        this.cbCodFunc = cbCodFunc;
        this.funcionarioFuncCod = funcionarioFuncCod;
        this.cbDtDoacao = cbDtDoacao;
    }

    public int getCbCod() {
        return cbCod;
    }

    public void setCbCod(int cbCod) {
        this.cbCod = cbCod;
    }

    public String getCbMotivo() {
        return cbMotivo;
    }

    public void setCbMotivo(String cbMotivo) {
        this.cbMotivo = cbMotivo;
    }

    public LocalDate getCbDtCriacao() {
        return cbDtCriacao;
    }

    public void setCbDtCriacao(LocalDate cbDtCriacao) {
        this.cbDtCriacao = cbDtCriacao;
    }

    public int getPessoaCarentePcCod() {
        return pessoaCarentePcCod;
    }

    public void setPessoaCarentePcCod(int pessoaCarentePcCod) {
        this.pessoaCarentePcCod = pessoaCarentePcCod;
    }

    public int getCbCodFunc() {
        return cbCodFunc;
    }

    public void setCbCodFunc(int cbCodFunc) {
        this.cbCodFunc = cbCodFunc;
    }

    public int getFuncionarioFuncCod() {
        return funcionarioFuncCod;
    }

    public void setFuncionarioFuncCod(int funcionarioFuncCod) {
        this.funcionarioFuncCod = funcionarioFuncCod;
    }

    public LocalDate getCbDtDoacao() {
        return cbDtDoacao;
    }

    public void setCbDtDoacao(LocalDate cbDtDoacao) {
        this.cbDtDoacao = cbDtDoacao;
    }
}

