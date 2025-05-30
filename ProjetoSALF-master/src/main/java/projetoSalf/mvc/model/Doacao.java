package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.DoacaoDAO;
import projetoSalf.mvc.dao.DoaProdDAO;
import projetoSalf.mvc.util.Conexao;

import java.util.ArrayList;
import java.util.List;

@Component
public class Doacao {

    @Autowired
    private DoacaoDAO dao;

    @Autowired
    private DoaProdDAO doaProdDAO;

    private int doa_cod;
    private String doa_dtentrada;
    private int func_cod;

    public Doacao() {}

    public Doacao(int func_cod) {
        this.doa_dtentrada = java.time.LocalDate.now().toString();
        this.func_cod = func_cod;
    }

    public int getDoaCod() {
        return doa_cod;
    }

    public void setDoaCod(int doa_cod) {
        this.doa_cod = doa_cod;
    }

    public String getDoaDtentrada() {
        return doa_dtentrada;
    }

    public void setDoaDtentrada(String doa_dtentrada) {
        this.doa_dtentrada = doa_dtentrada;
    }

    public int getFuncCod() {
        return func_cod;
    }

    public void setFuncCod(int func_cod) {
        this.func_cod = func_cod;
    }

    public List<Doacao> consultar(Integer funcCod, String data, Conexao conexao) {
        return dao.listar(funcCod, data); // o DAO ainda usa SingletonDB internamente, o Controller cria a conexão
    }

    public List<DoaProd> getProdutos(Conexao conexao) {
        DoaProdDAO doaProdDAO = new DoaProdDAO();
        return doaProdDAO.listarPorDoacao(this.doa_cod);
    }
}
