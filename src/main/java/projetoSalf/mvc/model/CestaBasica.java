package projetoSalf.mvc.model;

import java.sql.Date;

public class CestaBasica {
    private int cb_cod;
    private String cb_motivo;
    private Date cb_dtcriacao;
    private Date cb_dtdoacao;
    private int cb_codfunc;
    private int pessoa_carente_pc_cod;

    // Campos extras para exibir na lista
    private String nomePessoa;
    private String cpfPessoa;
    private String nomeFuncionario;

    public int getCb_cod() {
        return cb_cod;
    }

    public void setCb_cod(int cb_cod) {
        this.cb_cod = cb_cod;
    }

    public String getCb_motivo() {
        return cb_motivo;
    }

    public void setCb_motivo(String cb_motivo) {
        this.cb_motivo = cb_motivo;
    }

    public Date getCb_dtcriacao() {
        return cb_dtcriacao;
    }

    public void setCb_dtcriacao(Date cb_dtcriacao) {
        this.cb_dtcriacao = cb_dtcriacao;
    }

    public Date getCb_dtdoacao() {
        return cb_dtdoacao;
    }

    public void setCb_dtdoacao(Date cb_dtdoacao) {
        this.cb_dtdoacao = cb_dtdoacao;
    }

    public int getCb_codfunc() {
        return cb_codfunc;
    }

    public void setCb_codfunc(int cb_codfunc) {
        this.cb_codfunc = cb_codfunc;
    }

    public int getPessoa_carente_pc_cod() {
        return pessoa_carente_pc_cod;
    }

    public void setPessoa_carente_pc_cod(int pessoa_carente_pc_cod) {
        this.pessoa_carente_pc_cod = pessoa_carente_pc_cod;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getCpfPessoa() {
        return cpfPessoa;
    }

    public void setCpfPessoa(String cpfPessoa) {
        this.cpfPessoa = cpfPessoa;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }
}
