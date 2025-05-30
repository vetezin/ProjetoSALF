<<<<<<< HEAD

=======
>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
package projetoSalf.mvc.model;

import java.time.LocalDate;

public class CestaBasica {
    private int cbCod;
    private String cbMotivo;
    private LocalDate cbDtCriacao;
    private int pessoaCarentePcCod;
<<<<<<< HEAD
    private int funcCod; // corrigido aqui
    private LocalDate cbDtDoacao;

    public CestaBasica() {}
=======
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
>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e

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

<<<<<<< HEAD
    public LocalDate getCbDtDoacao() {
        return cbDtDoacao;
    }

    public void setCbDtDoacao(LocalDate cbDtDoacao) {
        this.cbDtDoacao = cbDtDoacao;
    }

=======
>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
    public int getPessoaCarentePcCod() {
        return pessoaCarentePcCod;
    }

    public void setPessoaCarentePcCod(int pessoaCarentePcCod) {
        this.pessoaCarentePcCod = pessoaCarentePcCod;
    }

<<<<<<< HEAD
    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }
}
=======
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

>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
