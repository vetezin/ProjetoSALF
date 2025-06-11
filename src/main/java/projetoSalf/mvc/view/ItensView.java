package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.ItensController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apis/itens")
public class ItensView {

    @Autowired
    private ItensController controller;

    @PostMapping
    public ResponseEntity<Object> adicionar(
            @RequestParam int listaId,
            @RequestParam int produtoId,
            @RequestParam int quantidade) {

        Map<String, Object> resposta = controller.addItemNaLista(listaId, produtoId, quantidade);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem("Item adicionado com sucesso!"));
    }

    @PutMapping
    public ResponseEntity<Object> atualizar(
            @RequestParam int listaId,
            @RequestParam int produtoId,
            @RequestParam int quantidade) {

        Map<String, Object> resposta = controller.atualizarItem(listaId, produtoId, quantidade);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem("Item atualizado com sucesso!"));
    }

    @DeleteMapping
    public ResponseEntity<Object> remover(
            @RequestParam int listaId,
            @RequestParam int produtoId) {

        Map<String, Object> resposta = controller.removerItem(listaId, produtoId);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem(resposta.get("mensagem").toString()));
    }

    @GetMapping("/{listaId}")
    public ResponseEntity<Object> listarPorLista(@PathVariable int listaId) {
        List<Map<String, Object>> itens = controller.getProdutosDaLista(listaId);
        return itens == null || itens.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhum item encontrado para esta lista."))
                : ResponseEntity.ok(itens);
    }
}