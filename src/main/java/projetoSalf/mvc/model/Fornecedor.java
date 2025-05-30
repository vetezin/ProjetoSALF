package projetoSalf.mvc.model;

public class Fornecedor {
    private int fornCod;
    private String fornNome;
    private String fornEnd;
    private String fornCpnj;
    private String fornTelefone;

    public Fornecedor() {
    }

    public Fornecedor(int fornCod, String fornNome, String fornEnd, String fornCpnj, String fornTelefone) {
        this.fornCod = fornCod;
        this.fornNome = fornNome;
        this.fornEnd = fornEnd;
        this.fornCpnj = fornCpnj;
        this.fornTelefone = fornTelefone;
    }

    public int getFornCod() {
        return fornCod;
    }

    public void setFornCod(int fornCod) {
        this.fornCod = fornCod;
    }

    public String getFornNome() {
        return fornNome;
    }

    public void setFornNome(String fornNome) {
        this.fornNome = fornNome;
    }

    public String getFornEnd() {
        return fornEnd;
    }

    public void setFornEnd(String fornEnd) {
        this.fornEnd = fornEnd;
    }

    public String getFornCpnj() {
        return fornCpnj;
    }

    public void setFornCpnj(String fornCpnj) {
        this.fornCpnj = fornCpnj;
    }

    public String getFornTelefone() {
        return fornTelefone;
    }

    public void setFornTelefone(String fornTelefone) {
        this.fornTelefone = fornTelefone;
    }
}

