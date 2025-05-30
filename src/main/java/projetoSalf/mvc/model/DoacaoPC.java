package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.DoacaoPCDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DoacaoPC {

    @Autowired
    private DoacaoPCDAO dao;

    private int doapcCod;
    private int funcCod;
    private int pcCod;
    private LocalDate doapcData;
    private List<DoaProd> produtos;

    public DoacaoPC() {
        this.doapcData = LocalDate.now();
    }

    public static DoacaoPC fromMap(Map<String, Object> map) {
        DoacaoPC d = new DoacaoPC();
        d.setFuncCod((int) map.get("funcCod"));
        d.setPcCod((int) map.get("pcCod"));
        d.setDoapcData(LocalDate.now());

        List<Map<String, Object>> itens = (List<Map<String, Object>>) map.get("produtos");
        List<DoaProd> produtos = new ArrayList<>();
        for (Map<String, Object> item : itens) {
            DoaProd p = new DoaProd();
            p.setProdutoProdCod((int) item.get("produtoProdCod"));
            p.setDoaProdQtd((int) item.get("doaProdQtd"));
            p.setDoaProdCatCod((int) item.get("doaProdCatCod"));
            produtos.add(p);
        }
        d.setProdutos(produtos);
        return d;
    }

    public List<DoacaoPC> listarPorFuncionario(int funcCod) {
        return dao.listarPorFuncionario(funcCod);
    }

    public List<DoacaoPC> listarPorPessoaCarente(int pcCod) {
        return dao.listarPorPessoaCarente(pcCod);
    }

    // Getters e Setters
    public int getDoapcCod() {
        return doapcCod;
    }

    public void setDoapcCod(int doapcCod) {
        this.doapcCod = doapcCod;
    }

    public int getFuncCod() {
        return funcCod;
    }

    public void setFuncCod(int funcCod) {
        this.funcCod = funcCod;
    }

    public int getPcCod() {
        return pcCod;
    }

    public void setPcCod(int pcCod) {
        this.pcCod = pcCod;
    }

    public LocalDate getDoapcData() {
        return doapcData;
    }

    public void setDoapcData(LocalDate doapcData) {
        this.doapcData = doapcData;
    }

    public List<DoaProd> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<DoaProd> produtos) {
        this.produtos = produtos;
    }

    // Métodos com DAO (sem passar conexão)

    public void setDao(DoacaoPCDAO dao) {
        this.dao = dao;
    }

    public boolean inserir() {
        return dao.inserir(this);
    }

    public List<DoacaoPC> listarTodos() {
        return dao.listarTodos();
    }

    public DoacaoPC buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public boolean deletar(int cod) {
        return dao.deletar(cod);
    }
}
