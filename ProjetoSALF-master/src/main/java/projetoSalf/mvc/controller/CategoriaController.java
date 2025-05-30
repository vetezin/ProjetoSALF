package projetoSalf.mvc.controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.CategoriaDAO;
import projetoSalf.mvc.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class CategoriaController {

    @Autowired
    private CategoriaDAO dao;

    public List<Map<String, Object>> getCat() {
        List<Categoria> lista = dao.get("");

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
        Categoria categoria = dao.get(id);
        if (categoria == null) {
            return Map.of("erro", "Categoria não encontrada.");
        }
        return Map.of("id", categoria.getId(), "desc", categoria.getDesc());
    }

    // Pode adicionar métodos de salvar, editar, deletar aqui no mesmo estilo
}
