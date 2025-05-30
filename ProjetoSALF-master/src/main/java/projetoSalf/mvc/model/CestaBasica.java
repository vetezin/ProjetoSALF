
package projetoSalf.mvc.model;

import java.time.LocalDate;

public class CestaBasica {
    private int cbCod;
    private String cbMotivo;
    private LocalDate cbDtCriacao;
    private int pessoaCarentePcCod;
    private int funcCod; // corrigido aqui
    private LocalDate cbDtDoacao;

    public CestaBasica() {}

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

    public LocalDate getCbDtDoacao() {
        return cbDtDoacao;
    }

    public void setCbDtDoacao(LocalDate cbDtDoacao) {
        this.cbDtDoacao = cbDtDoacao;
    }

    public int getPessoaCarentePcCod() {
        return pessoaCarentePcCod;
    }

    public void setPessoaCarentePcCod(int pessoaCarentePcCod) {
        this.pessoaCarentePcCod = pessoaCarentePcCod;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}
