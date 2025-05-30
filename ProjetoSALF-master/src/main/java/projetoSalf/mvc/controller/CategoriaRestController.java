package projetoSalf.mvc.view;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import projetoSalf.mvc.model.Categoria;
import projetoSalf.mvc.util.Conexao;

import java.util.*;

@RestController
@RequestMapping("/apis/categorias")
public class CategoriaRestController {

    @Autowired
    private Categoria categoriaModel;

    @GetMapping
    public List<Map<String, Object>> listarCategorias() {
        Conexao conexao = new Conexao();
        List<Categoria> lista = categoriaModel.consultar("", conexao);
        if (lista == null || lista.isEmpty()) return null;

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Categoria c : lista) {
            Map<String, Object> json = new HashMap<>();
            json.put("id", c.getId());
            json.put("desc", c.getDesc());
            resultado.add(json);
        }
        return resultado;
    }

    @GetMapping("/{id}")
    public Map<String, Object> buscarPorId(@PathVariable int id) {
        Categoria categoria = categoriaModel.consultar(id);
        if (categoria == null) return Map.of("erro", "Categoria não encontrada.");
        return Map.of("id", categoria.getId(), "desc", categoria.getDesc());
    }

    @PostMapping
    public Map<String, Object> cadastrar(@RequestBody Map<String, String> body) {
        String desc = body.get("desc");
        if (desc == null || desc.isBlank()) {
            return Map.of("erro", "Descrição inválida.");
        }

        Categoria nova = new Categoria(desc);
        Conexao conexao = new Conexao();
        List<Categoria> lista = categoriaModel.consultar("", conexao);
        int novoId = lista != null ? lista.size() + 1 : 1;
        nova.setId(novoId);

        return Map.of("id", nova.getId(), "desc", nova.getDesc());
    }

    @PutMapping("/{id}")
    public Map<String, Object> atualizar(@PathVariable int id, @RequestBody Map<String, String> body) {
        Categoria existente = categoriaModel.consultar(id);
        if (existente == null) return Map.of("erro", "Categoria não encontrada.");

        String novaDescricao = body.get("desc");
        if (novaDescricao == null || novaDescricao.isBlank()) {
            return Map.of("erro", "Descrição inválida.");
        }

        existente.setDesc(novaDescricao);
        return Map.of("id", existente.getId(), "desc", existente.getDesc());
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deletar(@PathVariable int id) {
        Categoria categoria = categoriaModel.consultar(id);
        if (categoria == null) return Map.of("erro", "Categoria não encontrada.");
        return Map.of("mensagem", "Categoria removida com sucesso!");
    }
}
