package projetoSalf.mvc.controller;


import org.springframework.web.bind.annotation.*;
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
                json.put("categoria", p.getCategoria().getID());
                prodList.add(json);
            }

            return prodList;
        }
    }


    public Map<String, Object> getProd(int id){
        Conexao conexao = new Conexao();
        Produto produto =  produtoModel.consultar(id);

        if (produto==null)
            return null;
        else {
            Map<String, Object> json = new HashMap<>();
            json.put("id", produto.getProd_cod());
            json.put("nome", produto.getProd_desc());
            json.put("preco", produto.getProd_valorun());
            json.put("data", produto.getProd_dtvalid());
            json.put("categoria", produto.getCategoria().getID());

            return json;
        }
    }



    public Map<String, Object> deletarProduto(Long id) {
        Conexao conexao = new Conexao();

        boolean deletado = produtoModel.deletarProduto(id);
        if (deletado) {
            return Map.of("mensagem", "Produto removido com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover o produto");
        }
    }


    public Map<String, Object> addProd(
            String prod_dtvalid,
            String prod_desc,
            float prod_valorun,
            Categoria categoria)
    {
        if (prod_dtvalid == null || prod_desc.isBlank() || prod_valorun <= 0 || categoria == null) {
            return Map.of("erro", "Dados inválidos para cadastro");
        }

        Conexao conexao = new Conexao();
        Produto novo = new Produto(prod_dtvalid, prod_desc, prod_valorun,categoria);
        Produto gravado = produtoModel.gravar(novo);

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getProd_cod());
            json.put("nome", gravado.getProd_desc());
            json.put("preco", gravado.getProd_valorun());
            json.put("data", gravado.getProd_dtvalid());
            json.put("categoria", gravado.getCategoria().getId());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }
    }



    public Map<String, Object> updtProd(
            Long prod_cod,
            String prod_dtvalid,
            String prod_desc,
           float prod_valorun,
            Categoria categoria)
     {

        if (prod_cod == null || prod_dtvalid == null || prod_desc == null || prod_desc.isBlank() || prod_valorun < 0 || categoria == null) {
            return Map.of("erro", "Dados inválidos para atualização");
        }

        if (produtoModel.deletarProduto(prod_cod)) {
            Produto produto = new Produto(prod_dtvalid, prod_desc, prod_valorun, categoria);
            produto.setProd_cod(prod_cod); // garante que mantém o mesmo ID

            Produto atualizado = produtoModel.gravar(produto);
            if (atualizado != null) {
                Map<String, Object> json = new HashMap<>();
                json.put("id", atualizado.getProd_cod());
                json.put("nome", atualizado.getProd_desc());
                json.put("preco", atualizado.getProd_valorun());
                json.put("data", atualizado.getProd_dtvalid());
                json.put("categoria", atualizado.getCategoria().getId());
                return json;
            } else {
                return Map.of("erro", "Erro ao atualizar o produto");
            }
        } else {
            return Map.of("erro", "Erro ao deletar o produto antigo");
        }
    }




}

