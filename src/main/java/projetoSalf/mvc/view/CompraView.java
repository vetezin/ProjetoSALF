package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.CompraController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/compra")
@CrossOrigin(origins = "*")
public class CompraView {

    @Autowired
    private CompraController controller;

    @GetMapping
    public ResponseEntity<Object> listarCompras() {
        List<Map<String, Object>> compras = controller.getCompras();
        if (compras == null || compras.isEmpty() || compras.get(0).containsKey("erro")) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Nenhuma compra encontrada"));
        }
        return ResponseEntity.ok(compras);
    }
    @GetMapping("/{id}/produtos")
    public ResponseEntity<Object> getProdutosCompra(@PathVariable int id) {
        List<Map<String, Object>> produtos = controller.getProdutosDaCompra(id);

        if (produtos == null || produtos.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Nenhum produto encontrado para esta compra."));
        }

        return ResponseEntity.ok(produtos);
    }

}
