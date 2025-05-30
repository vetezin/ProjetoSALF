package projetoSalf.mvc.controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.util.Conexao;

import java.util.*;

@Service
public class CompraController {

    private final Compra compraModel = new Compra();

    public List<Map<String, Object>> getCompras() {
        Conexao conexao = new Conexao();
        List<Compra> lista = compraModel.consultarTodas(conexao);
        if (lista == null || lista.isEmpty()) return List.of(Map.of("erro", "Nenhuma compra encontrada"));

        List<Map<String, Object>> resposta = new ArrayList<>();
        for (Compra c : lista) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getCoCod());
            item.put("descricao", c.getCoDescricao());
            item.put("valorTotal", c.getCoValorTotal());
            item.put("data", c.getCoDtCompra());
            item.put("totalItens", c.getCoTotalItens());
            item.put("funcionario", c.getFuncionarioFuncCod());
            item.put("cotacaoCod", c.getCotFornCotacaoCotCod());
            item.put("fornecedorCod", c.getCotFornFornecedorFornCod());
            resposta.add(item);
        }
        return resposta;
    }

    public Map<String, Object> addCompra(Map<String, Object> dados) {
        try {
            Compra nova = Compra.fromMap(dados);
            Conexao conexao = new Conexao();
            boolean sucesso = nova.inserir(conexao);
            if (sucesso) {
                return Map.of("sucesso", "Compra registrada com sucesso");
            } else {
                return Map.of("erro", "Erro ao registrar a compra");
            }
        } catch (Exception e) {
            return Map.of("erro", "Erro ao processar os dados da compra: " + e.getMessage());
        }
    }
}
