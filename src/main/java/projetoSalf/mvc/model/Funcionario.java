package projetoSalf.mvc.model;

public class Funcionario {
    private int funcCod;
    private String funcNome;
    private String funcCpf;
    private String funcSenha;
    private String funcEmail;
    private String funcLogin;
    private int funcNivel;

    public Funcionario() {
    }

    public Funcionario(int funcCod, String funcNome, String funcCpf, String funcSenha,
                       String funcEmail, String funcLogin, int funcNivel) {
        this.funcCod = funcCod;
        this.funcNome = funcNome;
        this.funcCpf = funcCpf;
        this.funcSenha = funcSenha;
        this.funcEmail = funcEmail;
        this.funcLogin = funcLogin;
        this.funcNivel = funcNivel;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }

    public String getFuncNome() {
        return funcNome;
    }

    public void setFuncNome(String funcNome) {
        this.funcNome = funcNome;
    }

    public String getFuncCpf() {
        return funcCpf;
    }

    public void setFuncCpf(String funcCpf) {
        this.funcCpf = funcCpf;
    }

    public String getFuncSenha() {
        return funcSenha;
    }

    public void setFuncSenha(String funcSenha) {
        this.funcSenha = funcSenha;
    }

    public String getFuncEmail() {
        return funcEmail;
    }

    public void setFuncEmail(String funcEmail) {
        this.funcEmail = funcEmail;
    }

    public String getFuncLogin() {
        return funcLogin;
    }

    public void setFuncLogin(String funcLogin) {
        this.funcLogin = funcLogin;
    }

    public int getFuncNivel() {
        return funcNivel;
    }

    public void setFuncNivel(int funcNivel) {
        this.funcNivel = funcNivel;
    }
}
