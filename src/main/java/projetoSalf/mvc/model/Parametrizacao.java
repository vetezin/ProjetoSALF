package projetoSalf.mvc.model;

import projetoSalf.mvc.dao.ParametrizacaoDAO;
import projetoSalf.mvc.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Parametrizacao {
    @Autowired
    private ParametrizacaoDAO dao;

    private int id;
    private String nomeEmpresa;
    private String cnpj;
    private String endereco;
    private String telefone;

    public Parametrizacao(String nomeEmpresa, String cnpj, String endereco, String telefone) {
        this.nomeEmpresa = nomeEmpresa;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public Parametrizacao() {
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public List<Parametrizacao> consultar(String filtro, Conexao conexao){
        return dao.get(filtro);
    }
    public boolean isEmpty(){
        return dao.get("").isEmpty();
    }
    public Parametrizacao gravar(Parametrizacao param){
        return dao.gravar(param);
    }
    public boolean deletarEmpresa(){
        return dao.deletarEmpresa();
    }
}
