package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Itens;
import projetoSalf.mvc.util.Conexao;

import java.util.*;

@Service
public class ItensController {

    @Autowired
    private Itens itensModel;

    public List<Map<String, Object>> getProdutosDaLista(int idLista) {
        Conexao conexao = new Conexao();
        List<Itens> itens = itensModel.consultarPorLista(idLista, conexao);

        List<Map<String, Object>> jsonList = new ArrayList<>();
        for (Itens item : itens) {
            Map<String, Object> json = new HashMap<>();
            json.put("listaId", item.getListaId());
            json.put("produtoId", item.getProdutoId());
            json.put("quantidade", item.getQuantidade());
            jsonList.add(json);
        }

        return jsonList;
    }

    public Map<String, Object> addItemNaLista(int listaId, int produtoId, int quantidade) {
        Conexao conexao = new Conexao();
        Itens novo = new Itens(listaId, produtoId, quantidade);
        Itens gravado = itensModel.gravar(novo);

        if (gravado != null) {
            return Map.of(
                    "listaId", gravado.getListaId(),
                    "produtoId", gravado.getProdutoId(),
                    "quantidade", gravado.getQuantidade()
            );
        } else {
            return Map.of("erro", "Erro ao adicionar item na lista");
        }
    }

    public Map<String, Object> atualizarItem(int listaId, int produtoId, int quantidade) {
        Itens existente = itensModel.consultar(listaId, produtoId);
        if (existente == null) return Map.of("erro", "Item não encontrado");

        existente.setQuantidade(quantidade);
        Itens atualizado = itensModel.update(existente);

        return atualizado != null
                ? Map.of("listaId", atualizado.getListaId(), "produtoId", atualizado.getProdutoId(), "quantidade", atualizado.getQuantidade())
                : Map.of("erro", "Erro ao atualizar item da lista");
    }

    public Map<String, Object> removerItem(int listaId, int produtoId) {
        Itens item = itensModel.consultar(listaId, produtoId);
        if (item == null) return Map.of("erro", "Item não encontrado");

        boolean deletado = itensModel.deletarListaCompraProd(item);
        return deletado
                ? Map.of("mensagem", "Item removido com sucesso!")
                : Map.of("erro", "Erro ao remover item da lista");
    }
}