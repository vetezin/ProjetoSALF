package projetoSalf.mvc.model;

import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class Parametrizacao {

    private int id;
    private String nomeEmpresa;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;
    private byte[] logotipo;

    // Campo para armazenar o logotipo em Base64 (não persistente no banco)
    private String logotipoBase64;

    public Parametrizacao(String nomeEmpresa, String cnpj, String endereco, String telefone, String email, byte[] logotipo) {
        this.nomeEmpresa = nomeEmpresa;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.setLogotipo(logotipo);  // chama o setter para já converter Base64
    }

    public Parametrizacao() {
    }

    public byte[] getLogotipo() {
        return logotipo;
    }

    public void setLogotipo(byte[] logotipo) {
        this.logotipo = logotipo;
        // Converte para Base64 automaticamente ao setar
        if (logotipo != null && logotipo.length > 0) {
            this.logotipoBase64 = Base64.getEncoder().encodeToString(logotipo);
        } else {
            this.logotipoBase64 = null;
        }
    }

    public String getLogotipoBase64() {
        return logotipoBase64;
    }

    // Não precisa de setter para logotipoBase64, pois é gerado automaticamente

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
