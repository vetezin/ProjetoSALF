package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.FuncionarioDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

@Component
public class Funcionario {

    @Autowired
    private FuncionarioDAO dao;

    private int func_cod;
    private String func_nome;
    private String func_cpf;
    private String func_senha;
    private String func_email;
    private String func_login;
    private int func_nivel;

    // Construtor sem parâmetros
    public Funcionario() {
    }

    // Construtor com todos os parâmetros
    public Funcionario(int func_cod, String func_nome, String func_cpf, String func_senha, String func_email, String func_login, int func_nivel) {
        this.func_cod = func_cod;
        this.func_nome = func_nome;
        this.func_cpf = func_cpf;
        this.func_senha = func_senha;
        this.func_email = func_email;
        this.func_login = func_login;
        this.func_nivel = func_nivel;
    }

    public Funcionario(String nome, String cpf, String senha, String email, String login, int nivel) {
        this.func_nome = nome;
        this.func_cpf = cpf;
        this.func_senha = senha;
        this.func_email = email;
        this.func_login = login;
        this.func_nivel = nivel;
    }

    // Métodos getters e setters
    public int getId() {
        return func_cod;
    }

    public void setFunc_cod(int func_cod) {
        this.func_cod = func_cod;
    }

    public String getNome() {
        return func_nome;
    }

    public void setFunc_nome(String func_nome) {
        this.func_nome = func_nome;
    }

    public String getCpf() {
        return func_cpf;
    }

    public void setFunc_cpf(String func_cpf) {
        this.func_cpf = func_cpf;
    }

    public String getSenha() {
        return func_senha;
    }

    public void setFunc_senha(String func_senha) {
        this.func_senha = func_senha;
    }

    public String getEmail() {
        return func_email;
    }

    public void setFunc_email(String func_email) {
        this.func_email = func_email;
    }

    public String getLogin() {
        return func_login;
    }

    public void setFunc_login(String func_login) {
        this.func_login = func_login;
    }

    public int getNivel() {
        return func_nivel;
    }

    public void setFunc_nivel(int func_nivel) {
        this.func_nivel = func_nivel;
    }

    public List<Funcionario> consultar(String filtro, Conexao conexao) {
        return this.dao.get(filtro);
    }
    public Funcionario consultar(int func_cod, Conexao conexao) {
        return this.dao.get(func_cod);
    }
    public Funcionario consultar(int id){
        return dao.get(id);
    }
    public boolean isEmpty() {
        return this.dao.get("").isEmpty();
    }
    public Funcionario gravar(Funcionario func) {
        return this.dao.gravar(func);
    }

    public boolean deletarFuncionario(Funcionario func) {
        return dao.apagar(func);
    }

    public Funcionario update(Funcionario func) {
        return dao.alterar(func);
    }
}