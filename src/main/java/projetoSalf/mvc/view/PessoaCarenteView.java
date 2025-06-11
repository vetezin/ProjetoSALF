package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.PessoaCarenteController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/apis/pessoa_carente")
public class PessoaCarenteView {

    @Autowired
    private PessoaCarenteController controller;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodos() {
        List<Map<String, Object>> lista = controller.listarTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable int id) {
        Map<String, Object> pessoa = controller.buscarPorId(id);
        if (pessoa.containsKey("erro")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pessoa);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrar(@RequestBody Map<String, Object> dados) {
        Map<String, Object> response = controller.cadastrar(dados);
        if (response.get("status").equals("ok")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(@PathVariable int id, @RequestBody Map<String, Object> dados) {
        Map<String, Object> response = controller.atualizar(id, dados);
        if (response.get("status").equals("ok")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable int id) {
        Map<String, Object> response = controller.deletar(id);
        if (response.get("status").equals("ok")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}