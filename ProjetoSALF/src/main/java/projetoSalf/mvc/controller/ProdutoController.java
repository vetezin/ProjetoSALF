
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
              //  json.put("data", p.getProd_dtvalid());




                cAux = categoriaModel.consultar(p.getCategoria());

                Map<String, Object> categoriaJson = new HashMap<>();
                categoriaJson.put("id", cAux.getId());
                categoriaJson.put("desc", cAux.getDesc());

                json.put("categoria", categoriaJson);
                prodList.add(json);
            }

            return prodList;
        }
    }

    public List<Map<String, Object>> getProdutosPorCategoria(int cat_cod) {
        Conexao conexao = new Conexao();
        String filtro = "cat_cod = " + cat_cod;
        List<Produto> lista = produtoModel.consultar(filtro, conexao);
        Categoria cAux = new Categoria();

        if (lista.isEmpty())
            return null;
        else {
            List<Map<String, Object>> prodList = new ArrayList<>();
            for (Produto p : lista) {
                Map<String, Object> json = new HashMap<>();
                Map<String,Object> catJson = new HashMap<>();

                json.put("id", p.getProd_cod());
                json.put("nome", p.getProd_desc());
                json.put("preco", p.getProd_valorun());
               // json.put("data", p.getProd_dtvalid());

                cAux = categoriaModel.consultar(p.getCategoria());
                catJson.put("id", cAux.getId());
                catJson.put("desc", cAux.getDesc());

                json.put("categoria", catJson);
                prodList.add(json);
            }
            return prodList;
        }
    }

    public List<Map<String, Object>> getProdutosMenorQue(float valorMax) {
        Conexao conexao = new Conexao();
        String filtro = "prod_valorun < " + valorMax;
        List<Produto> lista = produtoModel.consultar(filtro, conexao);

        if (lista.isEmpty())
            return null;

        List<Map<String, Object>> prodList = new ArrayList<>();
        Categoria cAux = new Categoria();

        for (Produto p : lista) {
            Map<String, Object> json = new HashMap<>();
            Map<String, Object> catJson = new HashMap<>();

            json.put("id", p.getProd_cod());
            json.put("nome", p.getProd_desc());
            json.put("preco", p.getProd_valorun());
         //   json.put("data", p.getProd_dtvalid());

            cAux = categoriaModel.consultar(p.getCategoria());
            catJson.put("id", cAux.getId());
            catJson.put("desc", cAux.getDesc());

            json.put("categoria", catJson);

            prodList.add(json);
        }
        return prodList;
    }


    //testar depois o getbyIDD
    public Map<String, Object> getProd(int id){
        Conexao conexao = new Conexao();
        Produto produto =  produtoModel.consultar(id);
        Categoria cAux = new Categoria();

        if (produto==null)
            return null;
        else {
            Map<String, Object> json = new HashMap<>();
            json.put("id", produto.getProd_cod());
            json.put("nome", produto.getProd_desc());
            json.put("preco", produto.getProd_valorun());
           // json.put("data", produto.getProd_dtvalid());
            json.put("categoria", produto.getCategoria());

            cAux = categoriaModel.consultar(produto.getCategoria());

            Map<String, Object> categoriaJson = new HashMap<>();
            categoriaJson.put("id", cAux.getId());
            categoriaJson.put("desc", cAux.getDesc());

            json.put("categoria", categoriaJson);

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
            String prod_desc,


            float prod_valorun,
            int cat_cod)
    {
        Categoria cAux = new Categoria();
        cAux= categoriaModel.consultar(cat_cod);

        if ( prod_desc.isBlank() || prod_valorun <= 0 || cat_cod <= 0 ||cAux == null) {
            return Map.of("erro", "Dados inválidos para cadastro");
        }

        Conexao conexao = new Conexao();
        Produto novo = new Produto( prod_desc, prod_valorun,cat_cod);

        Produto gravado = produtoModel.gravar(novo);
        //fazer verificacao para ver se a categoria existe

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getProd_cod());
            json.put("nome", gravado.getProd_desc());
            json.put("preco", gravado.getProd_valorun());
            //json.put("data", gravado.getProd_dtvalid());


            json.put("categoria", gravado.getCategoria());
            json.put("descricao",cAux.getDesc());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }
    }



    public Map<String, Object> updtProd(
            int prod_cod,

            String prod_desc,
            float prod_valorun,
            int  cat_cod)
    {
        // Validar ID existente
        Categoria cAux = new Categoria();
        cAux= categoriaModel.consultar(cat_cod);
        if (prod_cod <= 0

                || prod_desc == null
                || prod_desc.isBlank()
                || prod_valorun < 0
                || cAux == null) {
            return Map.of("erro", "Dados inválidos para atualização");
        }

        // busca o produto antes de atualizar
        Produto existente = produtoModel.consultar(prod_cod);
        if (existente == null) {
            return Map.of("erro", "Produto não encontrado");
        }

        // atualiza apenas os campos necessários
        //existente.setProd_dtvalid(prod_dtvalid);
        existente.setProd_desc(prod_desc);
        existente.setProd_valorun(prod_valorun);
        //verificar a categoria
        existente.setCategoria(cat_cod);

        // Usa o mesmo metodo
        Produto atualizado = produtoModel.alterar(existente);
        if (atualizado != null) {
            return Map.of(
                    "id",        atualizado.getProd_cod(),
                    "nome",      atualizado.getProd_desc(),
                    "preco",     atualizado.getProd_valorun(),
                    //"data",      atualizado.getProd_dtvalid(),
                    "categoria", atualizado.getCategoria()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar o produto");
        }
    }

    public List<Map<String, Object>> getProdutosPorNome(String termo) {

        Conexao conexao = new Conexao();
        List<Produto> listaOriginal = produtoModel.consultar("", conexao);
        List<Produto> listaFiltrada = new ArrayList<>();


        for (Produto p : listaOriginal) {
            if (p.getProd_desc().toLowerCase().contains(termo.toLowerCase())) {
                listaFiltrada.add(p);
            }
        }

        if (listaFiltrada.isEmpty()) return null;

        List<Map<String, Object>> prodList = new ArrayList<>();
        Categoria cAux = new Categoria();

        for (Produto p : listaFiltrada) {
            Map<String, Object> json = new HashMap<>();
            Map<String, Object> catJson = new HashMap<>();

            json.put("id", p.getProd_cod());
            json.put("nome", p.getProd_desc());
            json.put("preco", p.getProd_valorun());
           // json.put("data", p.getProd_dtvalid());

            cAux = categoriaModel.consultar(p.getCategoria());
            catJson.put("id", cAux.getId());
            catJson.put("desc", cAux.getDesc());

            json.put("categoria", catJson);

            prodList.add(json);
        }

        return prodList;
    }


    public List<Map<String, Object>> getProdutosOrdenados() {
        Conexao conexao = new Conexao();
        List<Produto> lista = produtoModel.consultar("", conexao);
        if (lista.isEmpty())
            return null;

        // ordenacao
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {

                String nome1 = lista.get(j).getProd_desc().toLowerCase();
                String nome2 = lista.get(j + 1).getProd_desc().toLowerCase();

                if (nome1.compareTo(nome2) > 0) {

                    Produto temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }

        List<Map<String, Object>> prodList = new ArrayList<>();
        Categoria cAux = new Categoria();

        for (Produto p : lista) {
            Map<String, Object> json = new HashMap<>();
            Map<String, Object> catJson = new HashMap<>();

            json.put("id", p.getProd_cod());
            json.put("nome", p.getProd_desc());
            json.put("preco", p.getProd_valorun());
            //json.put("data", p.getProd_dtvalid());

            cAux = categoriaModel.consultar(p.getCategoria());
            catJson.put("id", cAux.getId());
            catJson.put("desc", cAux.getDesc());

            json.put("categoria", catJson);

            prodList.add(json);
        }

        return prodList;
    }


}

