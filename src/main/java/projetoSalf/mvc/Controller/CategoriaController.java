package projetoSalf.mvc.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Categoria;
import projetoSalf.mvc.util.Conexao;

import java.util.*;

@Service
public class CategoriaController {

    @Autowired
    private Categoria categoriaModel;

    public List<Map<String, Object>> getCat() {
        Conexao conexao = new Conexao();
        List<Categoria> lista = categoriaModel.consultar("", conexao);

        if (lista == null || lista.isEmpty())
            return null;

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Categoria c : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", c.getId());
            json.put("desc", c.getDesc());
            resultado.add(json);
        }
        return resultado;
    }

    public Map<String, Object> getCategoriaPorId(int id) {
        Categoria categoria = categoriaModel.consultar(id);

        if (categoria == null) {
            return null;
        }

        Map<String, Object> json = new HashMap<>();
        json.put("id", categoria.getId());
        json.put("desc", categoria.getDesc());
        return json;
    }

    /*
    public Map<String, Object> cadastrarCategoria(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return Map.of("erro", "Descrição inválida.");
        }

        Categoria nova = new Categoria(descricao);
        Categoria gravada = categoriaModel.gravar(nova);

        if (gravada != null) {
            return Map.of(
                    "id", gravada.getId(),
                    "desc", gravada.getDesc()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar categoria.");
        }
    }

    public Map<String, Object> atualizarCategoria(int id, String novaDescricao) {
        Categoria existente = categoriaModel.consultar(id);

        if (existente == null) {
            return Map.of("erro", "Categoria não encontrada.");
        }

        if (novaDescricao == null || novaDescricao.isBlank()) {
            return Map.of("erro", "Descrição inválida.");
        }

        existente.setDesc(novaDescricao);
        Categoria atualizada = categoriaModel.alterar(existente);

        if (atualizada != null) {
            return Map.of(
                    "id", atualizada.getId(),
                    "desc", atualizada.getDesc()
            );
        } else {
            return Map.of("erro", "Erro ao atualizar categoria.");
        }
    }

    public Map<String, Object> deletarCategoria(int id) {
        Categoria categoria = categoriaModel.consultar(id);

        if (categoria == null) {
            return Map.of("erro", "Categoria não encontrada.");
        }

        boolean ok = categoriaModel.deletarCategoria(categoria);
        if (ok) {
            return Map.of("mensagem", "Categoria removida com sucesso!");
        } else {
            return Map.of("erro", "Erro ao remover a categoria.");
        }
    }
    */

}