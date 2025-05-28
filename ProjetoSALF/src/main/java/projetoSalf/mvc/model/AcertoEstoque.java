package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.AcertoEstoqueDAO;

import java.util.List;

@Component
public class AcertoEstoque {
    private int cod;               // ae_cod
    private String data;           // ae_data
    private String motivo;         // ae_motivo
    private int qtd;            // ae_qtd
    private int codProduto;        // prod_cod
    private int codFuncionario;    // func_cod

    @Autowired
    private AcertoEstoqueDAO dao;

    public AcertoEstoque() {}

    public AcertoEstoque(int cod, String data, String motivo, int qtd, int codProduto, int codFuncionario) {
        this.cod = cod;
        this.data = data;
        this.motivo = motivo;
        this.qtd = qtd;
        this.codProduto = codProduto;
        this.codFuncionario = codFuncionario;
    }

    public AcertoEstoque(String data, String motivo, int qtd, int codProduto, int codFuncionario) {
        this.data = data;
        this.motivo = motivo;
        this.qtd = qtd;
        this.codProduto = codProduto;
        this.codFuncionario = codFuncionario;
    }

    // Getters e Setters
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public double getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public int getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(int codProduto) {
        this.codProduto = codProduto;
    }

    public int getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(int codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    // Métodos de integração com DAO
    public List<AcertoEstoque> consultar(String filtro) {
        return dao.get(filtro);
    }

    public AcertoEstoque consultar(int id) {
        return dao.get(id);
    }

    public boolean isEmpty() {
        return dao.get("").isEmpty();
    }

    public AcertoEstoque gravar(AcertoEstoque ae) {
        return dao.gravar(ae);
    }

    public AcertoEstoque alterar(AcertoEstoque ae) {
        return dao.alterar(ae);
    }

    public boolean deletar(AcertoEstoque ae) {
        return dao.apagar(ae);
    }
}
