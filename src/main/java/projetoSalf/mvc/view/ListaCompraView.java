package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.ListaCompraController;
import projetoSalf.mvc.model.ListaCompra;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apis/lista")
public class ListaCompraView {

    @Autowired
    private ListaCompraController controller;

    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestParam String descricao, @RequestParam int funcionarioId) {
        Map<String, Object> resposta = controller.addListaCompra(descricao, funcionarioId);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem("Lista cadastrada com sucesso!"));
    }

    @GetMapping
    public ResponseEntity<Object> listar(@RequestParam(required = false) String filtro) {
        List<ListaCompra> listas = controller.listarListas(filtro);
        return listas == null || listas.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma lista encontrada."))
                : ResponseEntity.ok(listas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable int id) {
        ListaCompra lista = controller.buscarLista(id);
        return lista == null
                ? ResponseEntity.badRequest().body(new Mensagem("Lista não encontrada."))
                : ResponseEntity.ok(lista);
    }

    @PutMapping
    public ResponseEntity<Object> alterar(@RequestParam int id, @RequestParam String descricao, @RequestParam int funcionarioId) {
        Map<String, Object> resposta = controller.alterarLista(id, descricao, funcionarioId);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem("Lista alterada com sucesso!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable int id) {
        Map<String, String> resposta = controller.excluirLista(id);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro")))
                : ResponseEntity.ok(new Mensagem(resposta.get("sucesso")));
    }
}
