package projetoSalf.mvc.controller;


import projetoSalf.mvc.model.Produto;

import projetoSalf.mvc.util.Conexao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Vai fazer a conexão com a DAO, e dai que a dao vai interagir com o bd
@Service
public class ProdutoController {
    @Autowired
    private Produto produtoModel;



    public List<Map<String, Object>> getProd() {
        Conexao conexao = new Conexao();
        List<Produto> lista = produtoModel.consultar("", conexao);

        if (lista.isEmpty())
            return null;
        else {
            List<Map<String, Object>> prodList = new ArrayList<>();
            for (Produto p : lista) {
                Map<String, Object> json = new HashMap<>();
                json.put("id", p.getProd_cod());
                json.put("nome", p.getProd_desc());
                json.put("preco", p.getProd_valorun());
                json.put("data", p.getProd_dtvalid());
                json.put("categoria", p.getCat_cod());
                prodList.add(json); // corrigido aqui
            }

            return prodList;
        }
    }


    public Map<String, Object> deletarProduto(Long id) {
        Conexao conexao = new Conexao();

        boolean deletado = produtoModel.deletar(id, conexao);
        if (deletado) {
            return Map.of("mensagem", "Produto removido com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover o produto");
        }
    }

    public Map<String, Object> addProd(String nome, Double preco, int estoque) {
        if (nome == null || nome.isBlank() || preco == null || preco < 0 || estoque < 0) {
            return Map.of("erro", "Dados inválidos para cadastro");
        }

        Conexao conexao = new Conexao();
        Produto novo = new Produto(nome, preco, estoque);
        Produto gravado = produtoModel.gravar(novo, conexao);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getProd_cod());
            json.put("nome", gravado.getProd_desc());
            json.put("preco", gravado.getProd_valorun());
            json.put("data", gravado.getProd_dtvalid());
            json.put("categoria", gravado.getCat_cod().getId());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }
    }


    public Map<String, Object> updtProd(Long id, String nome, Double preco, int estoque) {
        if (id == null || nome == null || nome.isBlank() || preco == null || preco < 0 || estoque < 0) {
            return Map.of("erro", "Dados inválidos para atualização");
        }

        Conexao conexao = new Conexao();
        Produto produtoExistente = produtoModel.consultarPorId(id, conexao);

        if (produtoExistente == null) {
            return Map.of("erro", "Produto não encontrado");
        }


        produtoExistente.setProd_desc(nome);
        produtoExistente.setProd_valorun(preco.floatValue());
        produtoExistente.setCat_cod(new CategoriaProduto(estoque));

        Produto produtoAtualizado = produtoModel.gravar(produtoExistente);

        if (produtoAtualizado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", produtoAtualizado.getProd_cod());
            json.put("nome", produtoAtualizado.getProd_desc());
            json.put("preco", produtoAtualizado.getProd_valorun());
            json.put("data", produtoAtualizado.getProd_dtvalid());
            json.put("categoria", produtoAtualizado.getCat_cod().getId());
            return json;
        } else {
            return Map.of("erro", "Erro ao atualizar o produto");
        }
    }


}
