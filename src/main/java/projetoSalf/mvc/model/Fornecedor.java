package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.FornecedorDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

@Component
public class Fornecedor {

    @Autowired
    private FornecedorDAO dao;

    private int forn_cod;
    private String forn_nome;
    private String forn_end;
    private String forn_cnpj;
    private String forn_telefone;

    public Fornecedor() {}

    public Fornecedor(int forn_cod, String forn_nome, String forn_end, String forn_cnpj, String forn_telefone) {
        this.forn_cod = forn_cod;
        this.forn_nome = forn_nome;
        this.forn_end = forn_end;
        this.forn_cnpj = forn_cnpj;
        this.forn_telefone = forn_telefone;
    }

    public Fornecedor(String forn_nome, String forn_end, String forn_cnpj, String forn_telefone) {
        this.forn_nome = forn_nome;
        this.forn_end = forn_end;
        this.forn_cnpj = forn_cnpj;
        this.forn_telefone = forn_telefone;
    }

    public int getForn_cod() {
        return forn_cod;
    }

    public void setForn_cod(int forn_cod) {
        this.forn_cod = forn_cod;
    }

    public String getForn_nome() {
        return forn_nome;
    }

    public void setForn_nome(String forn_nome) {
        this.forn_nome = forn_nome;
    }

    public String getForn_end() {
        return forn_end;
    }

    public void setForn_end(String forn_end) {
        this.forn_end = forn_end;
    }

    public String getForn_cnpj() {
        return forn_cnpj;
    }

    public void setForn_cnpj(String forn_cnpj) {
        this.forn_cnpj = forn_cnpj;
    }

    public String getForn_telefone() {
        return forn_telefone;
    }

    public void setForn_telefone(String forn_telefone) {
        this.forn_telefone = forn_telefone;
    }

    public List<Fornecedor> consultar(String filtro, Conexao conexao){
        return dao.get(filtro);
    }
    public Fornecedor consultar(int id){
        return dao.get(id);
    }
    public boolean isEmpty(){return dao.get("").isEmpty();}
    public Fornecedor gravar(Fornecedor fornecedor){return dao.gravar(fornecedor);}
    public boolean deletar(Fornecedor fornecedor){return dao.apagar(fornecedor);}
}
