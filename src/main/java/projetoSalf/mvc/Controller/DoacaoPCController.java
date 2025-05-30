package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.DoaProdPCDAO;
import projetoSalf.mvc.dao.DoacaoPCDAO;
import projetoSalf.mvc.model.DoacaoPC;
import projetoSalf.mvc.model.DoaProd;

import java.time.LocalDate;
import java.util.*;

@Service
public class DoacaoPCController {

    @Autowired
    private DoacaoPCDAO dao;
    @Autowired
    private DoaProdPCDAO doaProdPCDAO;

    public Map<String, Object> registrarDoacaoPC(Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();

        try {
            DoacaoPC doacao = new DoacaoPC();
            doacao.setFuncCod((int) body.get("funcCod"));
            doacao.setPcCod((int) body.get("pcCod"));
            doacao.setDoapcData(LocalDate.now());
            doacao.setDao(dao); // injeta manualmente

            List<Map<String, Object>> itens = (List<Map<String, Object>>) body.get("produtos");
            List<DoaProd> produtos = new ArrayList<>();
            for (Map<String, Object> item : itens) {
                DoaProd p = new DoaProd();
                p.setProdutoProdCod((int) item.get("produtoProdCod"));
                p.setDoaProdQtd((int) item.get("doaProdQtd"));
                p.setDoaProdCatCod((int) item.get("doaProdCatCod"));
                produtos.add(p);
            }
            doacao.setProdutos(produtos);

            if (doacao.inserir()) {
                response.put("status", "ok");
                response.put("mensagem", "Doação registrada com sucesso!");
            } else {
                response.put("status", "erro");
                response.put("mensagem", "Erro ao registrar a doação.");
            }

        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Erro: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    public List<Map<String, Object>> listarTodos() {
        List<Map<String, Object>> lista = new ArrayList<>();
        List<DoacaoPC> doacoes = dao.listarTodos();

        for (DoacaoPC d : doacoes) {
            Map<String, Object> map = new HashMap<>();
            map.put("doapcCod", d.getDoapcCod());
            map.put("funcCod", d.getFuncCod());
            map.put("pcCod", d.getPcCod());
            map.put("doapcData", d.getDoapcData().toString());
            lista.add(map);
        }

        return lista;
    }

    public Map<String, Object> buscarPorId(int id) {
        Map<String, Object> map = new HashMap<>();
        DoacaoPC d = dao.buscarPorId(id);

        if (d != null) {
            map.put("doapcCod", d.getDoapcCod());
            map.put("funcCod", d.getFuncCod());
            map.put("pcCod", d.getPcCod());
            map.put("doapcData", d.getDoapcData().toString());
        } else {
            map.put("status", "erro");
            map.put("mensagem", "Doação não encontrada.");
        }

        return map;
    }

    public Map<String, Object> deletar(int cod) {
        Map<String, Object> map = new HashMap<>();
        try {
            boolean okReverter = doaProdPCDAO.reverterProdutosDaDoacaoPC(cod); // pode ser false se não houver produtos
            boolean okDelete = dao.deletar(cod);

            if (!okReverter) {
                System.out.println("Aviso: nenhum produto foi revertido para doação " + cod);
            }

            if (okDelete) {
                map.put("status", "ok");
                map.put("mensagem", "Doação deletada com sucesso.");
            } else {
                map.put("status", "erro");
                map.put("mensagem", "Erro ao deletar a doação.");
            }

        } catch (Exception e) {
            map.put("status", "erro");
            map.put("mensagem", "Exceção: " + e.getMessage());
            e.printStackTrace();
        }

        return map;
    }


    public List<Map<String, Object>> listarPorFuncionario(int funcCod) {
        List<Map<String, Object>> lista = new ArrayList<>();
        List<DoacaoPC> doacoes = dao.listarPorFuncionario(funcCod);

        for (DoacaoPC d : doacoes) {
            Map<String, Object> map = new HashMap<>();
            map.put("doapcCod", d.getDoapcCod());
            map.put("funcCod", d.getFuncCod());
            map.put("pcCod", d.getPcCod());
            map.put("doapcData", d.getDoapcData().toString());
            lista.add(map);
        }

        return lista;
    }

    public List<Map<String, Object>> listarPorPessoaCarente(int pcCod) {
        List<Map<String, Object>> lista = new ArrayList<>();
        List<DoacaoPC> doacoes = dao.listarPorPessoaCarente(pcCod);

        for (DoacaoPC d : doacoes) {
            Map<String, Object> map = new HashMap<>();
            map.put("doapcCod", d.getDoapcCod());
            map.put("funcCod", d.getFuncCod());
            map.put("pcCod", d.getPcCod());
            map.put("doapcData", d.getDoapcData().toString());
            lista.add(map);
        }

        return lista;
    }
}