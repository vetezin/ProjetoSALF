package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Estoque;
import projetoSalf.mvc.model.Produto;

import java.util.*;

@Service
public class EstoqueController {

    @Autowired
    private Estoque estoqueModel;

    @Autowired
    private Produto produtoModel;

    public List<Map<String, Object>> getEstoques() {
        List<Estoque> lista = estoqueModel.consultar("");
        if (lista.isEmpty()) return null;

        List<Map<String, Object>> estoqueList = new ArrayList<>();
        for (Estoque e : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", e.getEstoque_id());
            json.put("quantidade", e.getEs_qtdprod());
            json.put("validade", e.getEs_dtvalidade());
            json.put("produtoId", e.getProduto_prod_cod());

            Produto p = produtoModel.consultar(e.getProduto_prod_cod());
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
        Estoque estoque = estoqueModel.consultarPorId(id);
        if (estoque == null) return Map.of("erro", "Estoque não encontrado");

        Map<String, Object> json = new HashMap<>();
        json.put("id", estoque.getEstoque_id());
        json.put("quantidade", estoque.getEs_qtdprod());
        json.put("validade", estoque.getEs_dtvalidade());
        json.put("produtoId", estoque.getProduto_prod_cod());

        Produto p = produtoModel.consultar(estoque.getProduto_prod_cod());
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

        Produto p = produtoModel.consultar(produtoId);
        if (p == null) return Map.of("erro", "Produto não encontrado");

        Estoque novo = new Estoque(es_qtdprod, es_dtvalidade, produtoId);
        Estoque gravado = estoqueModel.gravar(novo);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getEstoque_id());
            json.put("quantidade", gravado.getEs_qtdprod());
            json.put("validade", gravado.getEs_dtvalidade());
            json.put("produtoId", gravado.getProduto_prod_cod());
            json.put("produto", Map.of(
                    "id", p.getProd_cod(),
                    "nome", p.getProd_desc(),
                    "preco", p.getProd_valorun()
            ));
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o estoque");
        }
    }

    public Map<String, Object> updtEstoque(
            int estoqueId,
            int es_qtdprod,
            String es_dtvalidade,
            int produtoId) {

        // Validação dos dados de entrada
        if (estoqueId <= 0 || es_qtdprod < 0 || es_dtvalidade == null || produtoId <= 0) {
            return Map.of("erro", "Dados inválidos para atualização");
        }

        // Verificar se o estoque existe
        Estoque existente = estoqueModel.consultarPorId(estoqueId);
        if (existente == null) {
            return Map.of("erro", "Estoque não encontrado");
        }

        // Verificar se o produto existe
        Produto p = produtoModel.consultar(produtoId);
        if (p == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        // Atualizar os campos do estoque
        existente.setEs_qtdprod(es_qtdprod);
        existente.setEs_dtvalidade(es_dtvalidade);
        existente.setProduto_prod_cod(produtoId);

        // Persistir a alteração
        Estoque atualizado = estoqueModel.alterar(existente);
        if (atualizado != null) {
            return Map.of(
                    "id", atualizado.getEstoque_id(),
                    "quantidade", atualizado.getEs_qtdprod(),
                    "validade", atualizado.getEs_dtvalidade(),
                    "produtoId", atualizado.getProduto_prod_cod(),
                    "produto", Map.of(
                            "id", p.getProd_cod(),
                            "nome", p.getProd_desc(),
                            "preco", p.getProd_valorun()
                    )
            );
        } else {
            return Map.of("erro", "Erro ao atualizar o estoque");
        }
    }

    public Map<String, Object> deletarEstoque(int id) {
        Estoque estoque = estoqueModel.consultarPorId(id); // Buscar o estoque pelo ID
        if (estoque == null) {
            return Map.of("erro", "Estoque não encontrado");
        }

        boolean deletado = estoqueModel.deletar(estoque); // Método que deve existir no modelo Estoque

        if (deletado) {
            return Map.of("mensagem", "Estoque removido com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover o estoque");
        }
    }

}
