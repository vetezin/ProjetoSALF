/*
package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import projetoSalf.mvc.model.Categoria;
import projetoSalf.mvc.model.Produto;
import projetoSalf.mvc.util.Conexao;

import java.util.HashMap;
import java.util.Map;

public class SaidaProdController {

    @Autowired
    private SaidaProdModel saidaProdModel;

    @Autowired
    private ProdutoModel produtoModel;

    @Autowired
    private CategoriaModel categoriaModel;

    public Map<String, Object> getSaidaProd(int saida_cod) {
        Conexao conexao = new Conexao();

        SaidaProd sp = saidaProdModel.consultarPorSaidaId(saida_cod);
        if (sp == null)
            return null;

        Produto produto = produtoModel.consultarPorCodigo(sp.getCodProduto(), conexao);
        if (produto == null)
            return null;

        Categoria categoria = categoriaModel.consultar(produto.getCategoria());
        if (categoria == null)
            return null;

        // JSON do produto com categoria embutida
        Map<String, Object> produtoJson = new HashMap<>();
        produtoJson.put("id", produto.getProd_cod());
        produtoJson.put("nome", produto.getProd_desc());
        produtoJson.put("preco", produto.getProd_valorun());
        produtoJson.put("data", produto.getProd_dtvalid());

        Map<String, Object> categoriaJson = new HashMap<>();
        categoriaJson.put("id", categoria.getId());
        categoriaJson.put("desc", categoria.getDesc());

        produtoJson.put("categoria", categoriaJson);

        // JSON do saidaProd com produto embutido
        Map<String, Object> saidaProdJson = new HashMap<>();
        saidaProdJson.put("quantidade", s);
        saidaProdJson.put("produto", produtoJson);

        return saidaProdJson;
    }


}
*/