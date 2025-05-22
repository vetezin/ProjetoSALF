package projetoSalf.mvc.controller;

import projetoSalf.mvc.model.Categoria;
import projetoSalf.mvc.model.Produto;
import projetoSalf.mvc.util.Conexao;

import java.util.HashMap;
import java.util.Map;

public class SaidaProdController {

    //Função fundamental, efetuar acerto de estoque
    /*

    */
    /*
    public Map<String, Object> addProd(
            String prod_desc,
            String prod_dtvalid,

            float prod_valorun,
            int cat_cod)
    {
        Categoria cAux = new Categoria();
        cAux= categoriaModel.consultar(cat_cod);

        if (prod_dtvalid == null || prod_desc.isBlank() || prod_valorun <= 0 || cat_cod <= 0 ||cAux == null) {
            return Map.of("erro", "Dados inválidos para cadastro");
        }

        Conexao conexao = new Conexao();
        Produto novo = new Produto(prod_dtvalid, prod_desc, prod_valorun,cat_cod);

        Produto gravado = produtoModel.gravar(novo);
        //fazer verificacao para ver se a categoria existe

        if (gravado != null) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", gravado.getProd_cod());
            json.put("nome", gravado.getProd_desc());
            json.put("preco", gravado.getProd_valorun());
            json.put("data", gravado.getProd_dtvalid());


            json.put("categoria", gravado.getCategoria());
            json.put("descricao",cAux.getDesc());
            return json;
        } else {
            return Map.of("erro", "Erro ao cadastrar o produto");
        }
    }
    
     */
}
