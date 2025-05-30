package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.EstoqueController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/estoque")
public class EstoqueView {

    @Autowired
    private EstoqueController estoqueController;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> lista = estoqueController.getEstoques();
        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum estoque encontrado."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") int id) {
        Map<String, Object> estoque = estoqueController.getEstoque(id);
        if (estoque != null && estoque.get("erro") == null) {
            return ResponseEntity.ok(estoque);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Estoque não encontrado."));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addEstoque(
            @RequestParam("es_qtdprod") int qtd,
            @RequestParam("es_dtvalidade") String validade,
            @RequestParam("produtoId") int produtoId
    ) {
        Map<String, Object> json = estoqueController.addEstoque(qtd, validade, produtoId);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem("Estoque cadastrado com sucesso!"));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @PutMapping
    public ResponseEntity<Object> atualizarEstoque(
            @RequestParam("estoque_id") int id,
            @RequestParam("es_qtdprod") int qtd,
            @RequestParam("es_dtvalidade") String validade,
            @RequestParam("produtoId") int produtoId
    ) {
        Map<String, Object> json = estoqueController.updtEstoque(id, qtd, validade, produtoId);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem("Estoque atualizado com sucesso!"));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarEstoque(@PathVariable("id") int id) {
        Map<String, Object> json = estoqueController.deletarEstoque(id);
        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }


}
