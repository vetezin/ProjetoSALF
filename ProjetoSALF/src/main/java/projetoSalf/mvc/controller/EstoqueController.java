package projetoSalf.mvc.controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Estoque;
import projetoSalf.mvc.model.Produto;

import java.util.*;

@Service
public class EstoqueController {

    public List<Map<String, Object>> getEstoques() {
        List<Estoque> lista = new Estoque().consultar("");  // Supondo método instanciado
        if (lista.isEmpty()) return null;

        List<Map<String, Object>> estoqueList = new ArrayList<>();
        for (Estoque e : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getEstoque_id());
            json.put("quantidade", e.getEs_qtdprod());
            json.put("validade", e.getEs_dtvalidade());
            json.put("produtoId", e.getProduto_prod_cod());

            Produto p = new Produto().consultar(e.getProduto_prod_cod());
            if (p != null) {
                Map<String, Object> prodJson = new HashMap<>();
                prodJson.put("id", p.getProd_cod());
                prodJson.put("nome", p.getProd_desc());
                prodJson.put("preco", p.getProd_valorun());
                json.put("produto", prodJson);
            }

            estoqueList.add(json);
        }

        return estoqueList;
    }

    public Map<String, Object> getEstoque(int id) {
        Estoque estoque = new Estoque().consultarPorId(id);
        if (estoque == null) return Map.of("erro", "Estoque não encontrado");

        Map<String, Object> json = new HashMap<>();
        json.put("id", estoque.getEstoque_id());
        json.put("quantidade", estoque.getEs_qtdprod());
        json.put("validade", estoque.getEs_dtvalidade());
        json.put("produtoId", estoque.getProduto_prod_cod());

        Produto p = new Produto().consultar(estoque.getProduto_prod_cod());
        if (p != null) {
            Map<String, Object> prodJson = new HashMap<>();
            prodJson.put("id", p.getProd_cod());
            prodJson.put("nome", p.getProd_desc());
            prodJson.put("preco", p.getProd_valorun());
            json.put("produto", prodJson);
        }

        return json;
    }

    public Map<String, Object> addEstoque(int es_qtdprod, String es_dtvalidade, int produtoId) {
        if (es_qtdprod <= 0 || es_dtvalidade == null || produtoId <= 0) {
            return Map.of("erro", "Dados inválidos para cadastro");
        }

        Produto p = new Produto().consultar(produtoId);
        if (p == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        Estoque novo = new Estoque(es_qtdprod, es_dtvalidade, produtoId);
        Estoque gravado = new Estoque().gravar(novo);

        if (gravado != null) {
            return Map.of(
                    "id", gravado.getEstoque_id(),
                    "quantidade", gravado.getEs_qtdprod(),
                    "validade", gravado.getEs_dtvalidade(),
                    "produtoId", gravado.getProduto_prod_cod()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar o estoque");
        }
    }
    public Map<String, Object> updtEstoque(int id, int es_qtdprod, String es_dtvalidade, int produtoId) {
        Estoque existente = new Estoque().consultarPorId(id);
        if (existente == null) {
            return Map.of("erro", "Estoque não encontrado");
        }

        Produto p = new Produto().consultar(produtoId);
        if (p == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        existente.setEs_qtdprod(es_qtdprod);
        existente.setEs_dtvalidade(es_dtvalidade);
        existente.setProduto_prod_cod(produtoId);

        Estoque atualizado = new Estoque().alterar(existente);
        if (atualizado != null) {
            return Map.of(
                    "id", atualizado.getEstoque_id(),
                    "quantidade", atualizado.getEs_qtdprod(),
                    "validade", atualizado.getEs_dtvalidade(),
                    "produtoId", atualizado.getProduto_prod_cod()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar o estoque");
        }
    }

    public Map<String, Object> deletarEstoque(int id) {
        Estoque estoque = new Estoque().consultarPorId(id);
        if (estoque == null) {
            return Map.of("erro", "Estoque não encontrado");
        }

        boolean deletado = new Estoque().deletar(estoque);
        if (deletado) {
            return Map.of("mensagem", "Estoque removido com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover o estoque");
        }
    }
}
