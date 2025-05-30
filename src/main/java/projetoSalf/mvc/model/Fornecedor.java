package projetoSalf.mvc.model;

import projetoSalf.mvc.dao.FornecedorDAO;

import java.util.List;

public class Fornecedor {

    private int forn_cod;
    private String forn_nome;
    private String forn_logradouro;
    private String forn_numero;
    private String forn_cep;
    private String forn_cidade;
    private String forn_complemento;
    private String forn_cnpj;
    private String forn_telefone;
    private String forn_contato;
    private String forn_email;

    public Fornecedor() {}

    public Fornecedor(int forn_cod, String forn_nome, String forn_logradouro, String forn_numero, String forn_cep,
                      String forn_cidade, String forn_complemento, String forn_cnpj, String forn_telefone,
                      String forn_contato, String forn_email) {
        this.forn_cod = forn_cod;
        this.forn_nome = forn_nome;
        this.forn_logradouro = forn_logradouro;
        this.forn_numero = forn_numero;
        this.forn_cep = forn_cep;
        this.forn_cidade = forn_cidade;
        this.forn_complemento = forn_complemento;
        this.forn_cnpj = forn_cnpj;
        this.forn_telefone = forn_telefone;
        this.forn_contato = forn_contato;
        this.forn_email = forn_email;
    }

    public Fornecedor(String forn_nome, String forn_logradouro, String forn_numero, String forn_cep,
                      String forn_cidade, String forn_complemento, String forn_cnpj, String forn_telefone,
                      String forn_contato, String forn_email) {
        this(0, forn_nome, forn_logradouro, forn_numero, forn_cep, forn_cidade, forn_complemento,
                forn_cnpj, forn_telefone, forn_contato, forn_email);
    }

    // Getters e Setters

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

    public String getForn_logradouro() {
        return forn_logradouro;
    }

    public void setForn_logradouro(String forn_logradouro) {
        this.forn_logradouro = forn_logradouro;
    }

    public String getForn_numero() {
        return forn_numero;
    }

    public void setForn_numero(String forn_numero) {
        this.forn_numero = forn_numero;
    }

    public String getForn_cep() {
        return forn_cep;
    }

    public void setForn_cep(String forn_cep) {
        this.forn_cep = forn_cep;
    }

    public String getForn_cidade() {
        return forn_cidade;
    }

    public void setForn_cidade(String forn_cidade) {
        this.forn_cidade = forn_cidade;
    }

    public String getForn_complemento() {
        return forn_complemento;
    }

    public void setForn_complemento(String forn_complemento) {
        this.forn_complemento = forn_complemento;
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

    public String getForn_contato() {
        return forn_contato;
    }

    public void setForn_contato(String forn_contato) {
        this.forn_contato = forn_contato;
    }

    public String getForn_email() {
        return forn_email;
    }

    public void setForn_email(String forn_email) {
        this.forn_email = forn_email;
    }

    // Métodos DAO com SingletonDB (sem Conexao como parâmetro)

    public static List<Fornecedor> consultarTodos() {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.get("");
    }

    public static Fornecedor consultarPorId(int id) {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.get(id);
    }

    public static List<Fornecedor> consultarComFiltro(String filtro) {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.get(filtro);
    }

    public static boolean deletar(Fornecedor fornecedor) {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.apagar(fornecedor);
    }

    public Fornecedor inserir() {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.gravar(this);
    }

    public Fornecedor alterar() {
        FornecedorDAO dao = new FornecedorDAO();
        return dao.alterar(this);
    }
}
