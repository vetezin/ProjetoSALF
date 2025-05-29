package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.FornecedorDAO;

import java.util.List;

@Component
public class Fornecedor {

    private int forn_cod;
    private String forn_nome;
    private String forn_end; // Nome da coluna agora é 'end'
    private String forn_cnpj;
    private String forn_telefone;

    @Autowired
    private FornecedorDAO dao;

    public Fornecedor() {
        // Construtor padrão necessário para o Spring
    }

    // Construtor completo
    public Fornecedor(int forn_cod, String forn_nome, String forn_end, String forn_cnpj, String forn_telefone) {
        this.forn_cod = forn_cod;
        this.forn_nome = forn_nome;
        this.forn_end = forn_end;
        this.forn_cnpj = forn_cnpj;
        this.forn_telefone = forn_telefone;
    }

    // Construtor para gravação (sem ID, assumindo que o banco gera)
    public Fornecedor(String forn_nome, String forn_end, String forn_cnpj, String forn_telefone) {
        this(0, forn_nome, forn_end, forn_cnpj, forn_telefone);
    }

    // --- Getters e Setters ---

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

    public String getForn_end() { // Alterado para forn_end
        return forn_end;
    }

    public void setForn_end(String forn_end) { // Alterado para forn_end
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

    // --- MÉTODOS DE INTEGRAÇÃO COM DAO (usando o DAO injetado) ---
    // Estes métodos delegam as operações para o DAO.

    public List<Fornecedor> consultar(String filtro) {
        return dao.get(filtro);
    }

    public Fornecedor consultar(int id) {
        return dao.get(id);
    }

    public Fornecedor gravar(Fornecedor fornecedor) {
        return dao.gravar(fornecedor);
    }

    public Fornecedor alterar() {
        return dao.alterar(this);
    }

    public Fornecedor alterar(Fornecedor fornecedor) { // Mantido para flexibilidade, caso o controller passe uma instância
        return dao.alterar(fornecedor);
    }

    public boolean apagar(Fornecedor fornecedor) {
        return dao.apagar(fornecedor);
    }

    public boolean isEmpty(){
        return dao.get("").isEmpty();
    }

    // Implementação dos métodos getId() e getNome() para polimorfismo
    public int getId() {
        return this.forn_cod;
    }

    public String getNome() {
        return this.forn_nome;
    }
}