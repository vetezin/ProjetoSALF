package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.ListaCompraProd;
import projetoSalf.mvc.util.Conexao;

import java.util.*;

@Service
public class ListaCompraProdController {

    @Autowired
    private ListaCompraProd listaCompraProdModel;

    public List<Map<String, Object>> getItens() {
        Conexao conexao = new Conexao();
        List<ListaCompraProd> lista = listaCompraProdModel.getAll("", conexao);
        if (!lista.isEmpty()) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (ListaCompraProd item : lista) {
                Map<String, Object> json = new HashMap<>();
                json.put("lc_cod", item.getLc_cod());
                json.put("prod_cod", item.getProd_cod());
                json.put("lc_prod_qtd", item.getLc_prod_qtd());
                result.add(json);
            }
            return result;
        }
        return null;
    }

    public Map<String, Object> addItem(int lc_cod, int prod_cod, int qtd) {
        Conexao conexao = new Conexao();
        ListaCompraProd novo = new ListaCompraProd(lc_cod, prod_cod, qtd);
        ListaCompraProd gravado = listaCompraProdModel.gravar(novo);

        if (gravado != null) {
            return Map.of(
                    "lc_cod", gravado.getLc_cod(),
                    "prod_cod", gravado.getProd_cod(),
                    "lc_prod_qtd", gravado.getLc_prod_qtd()
            );
        }
        return Map.of("erro", "Erro ao adicionar item à lista");
    }

    public Map<String, Object> deletarItem(int lc_cod, int prod_cod) {
        ListaCompraProd existente = listaCompraProdModel.consultar(lc_cod, prod_cod);
        if (existente == null) {
            return Map.of("erro", "Item da lista não encontrado");
        }

        boolean deletado = listaCompraProdModel.deletar(existente);
        if (deletado) {
            return Map.of("mensagem", "Item removido com sucesso!");
        }
        return Map.of("erro", "Erro ao remover item da lista");
    }
}
