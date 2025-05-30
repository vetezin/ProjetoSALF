package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.SaidaDAO;

import java.util.List;

@Component
public class Saida {
    private int cod;
    private String dataSaida;
    private String motivo;
    private int codFuncionario;

    @Autowired
    private SaidaDAO dao;

    public Saida() {}

    public Saida(int cod, String dataSaida, String motivo, int codFuncionario) {
        this.cod = cod;
        this.dataSaida = dataSaida;
        this.motivo = motivo;
        this.codFuncionario = codFuncionario;
    }

    public Saida(String dataSaida, String motivo, int codFuncionario) {
        this.dataSaida = dataSaida;
        this.motivo = motivo;
        this.codFuncionario = codFuncionario;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(int codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    // Métodos de integração com DAO
    public List<Saida> consultar(String filtro) {
        return dao.get(filtro);
    }

    public Saida consultar(int id) {
        return dao.get(id);
    }

    public boolean isEmpty() {
        return dao.get("").isEmpty();
    }

    public Saida gravar(Saida s) {
        return dao.gravar(s);
    }

    public Saida alterar(Saida s) {
        return dao.alterar(s);
    }

    public boolean deletar(Saida s) {
        return dao.apagar(s);
    }
}
