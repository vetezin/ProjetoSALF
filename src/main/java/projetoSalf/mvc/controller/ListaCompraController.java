package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.ListaCompra;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/listaCompra")
public class ListaCompraController {

    @Autowired
    private ListaCompra listaCompraModel;

    @PostMapping("/cadastrar")
    public Map<String, Object> addListaCompra(@RequestParam String descricao, @RequestParam int funcionarioId) {
        ListaCompra nova = new ListaCompra(descricao, LocalDate.now(), funcionarioId);
        ListaCompra gravada = listaCompraModel.gravar(nova);

        if (gravada != null) {
            return Map.of(
                    "id", gravada.getId(),
                    "descricao", gravada.getDescricao(),
                    "dataCriacao", gravada.getDataCriacao(),
                    "funcionarioId", gravada.getFuncionarioId()
            );
        } else {
            return Map.of("erro", "Erro ao cadastrar a lista");
        }
    }

    @GetMapping("/listar")
    public List<ListaCompra> listarListas(@RequestParam(required = false) String filtro) {
        return listaCompraModel.consultar(filtro);
    }

    @GetMapping("/buscar/{id}")
    public ListaCompra buscarLista(@PathVariable int id) {
        return listaCompraModel.consultar(id);
    }

    @PutMapping("/alterar")
    public Map<String, Object> alterarLista(@RequestParam int id, @RequestParam String descricao, @RequestParam int funcionarioId) {
        ListaCompra nova = new ListaCompra(id, descricao, LocalDate.now(), funcionarioId);
        ListaCompra alterada = listaCompraModel.alterar(nova);

        if (alterada != null) {
            return Map.of(
                    "id", alterada.getId(),
                    "descricao", alterada.getDescricao(),
                    "dataCriacao", alterada.getDataCriacao(),
                    "funcionarioId", alterada.getFuncionarioId()
            );
        } else {
            return Map.of("erro", "Erro ao alterar a lista");
        }
    }

    @DeleteMapping("/excluir/{id}")
    public Map<String, String> excluirLista(@PathVariable int id) {
        ListaCompra lista = listaCompraModel.consultar(id);
        if (lista != null && listaCompraModel.apagar(lista)) {
            return Map.of("sucesso", "Lista excluída com sucesso");
        } else {
            return Map.of("erro", "Erro ao excluir a lista");
        }
    }
}
