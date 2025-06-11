package projetoSalf.mvc.model;

public class PessoaCarente {
    private int pcCod;
    private String pcNome;
    private String pcCpf;
    private String pcTelefone;
    private String pcEndereco;
    private String pcCep;

    public PessoaCarente() {
    }

    public PessoaCarente(int pcCod, String pcNome, String pcCpf, String pcTelefone, String pcEndereco, String pcCep) {
        this.pcCod = pcCod;
        this.pcNome = pcNome;
        this.pcCpf = pcCpf;
        this.pcTelefone = pcTelefone;
        this.pcEndereco = pcEndereco;
        this.pcCep = pcCep;
    }

    public int getPcCod() {
        return pcCod;
    }

    public void setPcCod(int pcCod) {
        this.pcCod = pcCod;
    }

    public String getPcNome() {
        return pcNome;
    }

    public void setPcNome(String pcNome) {
        this.pcNome = pcNome;
    }

    public String getPcCpf() {
        return pcCpf;
    }

    public void setPcCpf(String pcCpf) {
        this.pcCpf = pcCpf;
    }

    public String getPcTelefone() {
        return pcTelefone;
    }

    public void setPcTelefone(String pcTelefone) {
        this.pcTelefone = pcTelefone;
    }

    public String getPcEndereco() {
        return pcEndereco;
    }

    public void setPcEndereco(String pcEndereco) {
        this.pcEndereco = pcEndereco;
    }

    public String getPcCep() {
        return pcCep;
    }

    public void setPcCep(String CEP) {
        this.pcCep = pcCep;
    }
}

