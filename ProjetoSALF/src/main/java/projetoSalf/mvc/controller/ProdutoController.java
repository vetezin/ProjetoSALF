package projetoSalf.mvc.controller;


import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.Categoria;
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

    @Autowired
    private Categoria categoriaModel;



    public List<Map<String, Object>> getProd() {
        Conexao conexao = new Conexao();
        List<Produto> lista = produtoModel.consultar("", conexao);
        Categoria cAux = new Categoria();
        if (lista.isEmpty())
            return null;
        else
        {
            List<Map<String, Object>> prodList = new ArrayList<>();
            for (Produto p : lista) {
                Map<String, Object> json = new HashMap<>();
                Map<String,Object> catJson = new HashMap<>();

                json.put("id", p.getProd_cod());
                json.put("nome", p.getProd_desc());
                json.put("preco", p.getProd_valorun());
                json.put("data", p.getProd_dtvalid());




                cAux = categoriaModel.consultar(p.getCategoria().getId());

                Map<String, Object> categoriaJson = new HashMap<>();
                categoriaJson.put("id", cAux.getId());
                categoriaJson.put("desc", cAux.getDesc());

                json.put("categoria", categoriaJson);
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
            json.put("categoria", produto.getCategoria().getId());

            return json;
        }
    }





    public Map<String, Object> deletarProduto(int  id) {
        Produto produto = produtoModel.consultar(id); // Busca o produto pelo ID
        if (produto == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        boolean deletado = produtoModel.deletarProduto(produto); // Deleta o produto

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
            json.put("descricao",gravado.getCategoria().getDesc());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }
    }



    public Map<String, Object> updtProd(
            int prod_cod,
            String prod_dtvalid,
            String prod_desc,
            float prod_valorun,
            Categoria categoria)
    {
        // Validar ID existente
        if (prod_cod <= 0
                || prod_dtvalid == null
                || prod_desc == null
                || prod_desc.isBlank()
                || prod_valorun < 0
                || categoria == null) {
            return Map.of("erro", "Dados inválidos para atualização");
        }

        // Buscar o produto antes de atualizar
        Produto existente = produtoModel.consultar(prod_cod);
        if (existente == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        // Atualiza apenas os campos necessários
        existente.setProd_dtvalid(prod_dtvalid);
        existente.setProd_desc(prod_desc);
        existente.setProd_valorun(prod_valorun);
        existente.setCategoria(categoria);

        // Usa o mesmo metodo
        Produto atualizado = produtoModel.gravar(existente);
        if (atualizado != null) {
            return Map.of(
                    "id",        atualizado.getProd_cod(),
                    "nome",      atualizado.getProd_desc(),
                    "preco",     atualizado.getProd_valorun(),
                    "data",      atualizado.getProd_dtvalid(),
                    "categoria", atualizado.getCategoria().getId()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar o produto");
        }
    }


}

