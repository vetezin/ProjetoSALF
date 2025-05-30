package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.ListaCompraController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apis/lista")
public class ListaCompraView {

    @Autowired
    private ListaCompraController controller;

    // Endpoint para listar todas as listas de compras com filtro
    @GetMapping
    public ResponseEntity<Object> listar(@RequestParam(required = false) String filtro) {
        List<Map<String, Object>> listas = controller.getListasCompra(filtro); // Chamando método correto do controller
        return listas == null || listas.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma lista encontrada."))
                : ResponseEntity.ok(listas);
    }

    // Endpoint para retornar todas as listas de compras sem filtro
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> listas = controller.getListasCompra(null); // Chamando sem filtro
        return listas == null || listas.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma lista encontrada."))
                : ResponseEntity.ok(listas);
    }

    // Endpoint para buscar lista de compras por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable int id) {
        Map<String, Object> resposta = controller.getListaCompra(id); // Chamando método correto
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Object> cadastrar(@RequestParam String descricao, @RequestParam int funcionarioId, @RequestParam(required = false) String dataCriacao) {
        Map<String, Object> resposta = controller.addListaCompra(descricao, funcionarioId, dataCriacao);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem(resposta.get("sucesso").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> alterar(@RequestParam int id, @RequestParam String descricao, @RequestParam int funcionarioId, @RequestParam String dataCriacao) {
        Map<String, Object> resposta = controller.updtListaCompra(id, descricao, dataCriacao, funcionarioId);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem(resposta.get("sucesso").toString()));
    }


    // Endpoint para excluir uma lista de compras
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable int id) {
        Map<String, String> resposta = controller.deletarListaCompra(id); // Chamando método correto para exclusão
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro")))
                : ResponseEntity.ok(new Mensagem(resposta.get("sucesso")));
    }
}