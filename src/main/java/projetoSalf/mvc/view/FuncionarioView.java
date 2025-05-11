package projetoSalf.mvc.view;

public class FuncionarioView {
    private int func_cod;
    private String func_nome;
    private String func_cpf;
    private String func_senha;
    private String func_email;
    private String func_login;
    private int func_nivel;

    // Construtor sem parâmetros
    public FuncionarioView() {
    }

    // Construtor com todos os parâmetros
    public FuncionarioView(int func_cod, String func_nome, String func_cpf, String func_senha, String func_email, String func_login, int func_nivel) {
        this.func_cod = func_cod;
        this.func_nome = func_nome;
        this.func_cpf = func_cpf;
        this.func_senha = func_senha;
        this.func_email = func_email;
        this.func_login = func_login;
        this.func_nivel = func_nivel;
    }

    // Métodos getters e setters
    public int getFunc_cod() {
        return func_cod;
    }

    public void setFunc_cod(int func_cod) {
        this.func_cod = func_cod;
    }

    public String getFunc_nome() {
        return func_nome;
    }

    public void setFunc_nome(String func_nome) {
        this.func_nome = func_nome;
    }

    public String getFunc_cpf() {
        return func_cpf;
    }

    public void setFunc_cpf(String func_cpf) {
        this.func_cpf = func_cpf;
    }

    public String getFunc_senha() {
        return func_senha;
    }

    public void setFunc_senha(String func_senha) {
        this.func_senha = func_senha;
    }

    public String getFunc_email() {
        return func_email;
    }

    public void setFunc_email(String func_email) {
        this.func_email = func_email;
    }

    public String getFunc_login() {
        return func_login;
    }

    public void setFunc_login(String func_login) {
        this.func_login = func_login;
    }

    public int getFunc_nivel() {
        return func_nivel;
    }

    public void setFunc_nivel(int func_nivel) {
        this.func_nivel = func_nivel;
    }
}

