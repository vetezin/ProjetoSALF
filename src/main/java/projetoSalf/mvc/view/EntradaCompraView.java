package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.EntradaCompraController;
import projetoSalf.mvc.model.Compra;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/entrada")
@CrossOrigin(origins = "*")
public class EntradaCompraView {

    @Autowired
    private EntradaCompraController controller;


    // ✅ NOVO ENDPOINT: Confirmar entrada no estoque com base na compra existente
    @PostMapping("/confirmarEstoque")
    public ResponseEntity<Object> confirmarEstoque(@RequestBody Map<String, Object> dados) {
        try {
            Map<String, Object> resposta = controller.confirmarEstoque(dados);
            if (resposta.containsKey("erro")) {
                return ResponseEntity.badRequest().body(resposta);
            }
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("erro", "Erro interno: " + e.getMessage()));
        }
    }
}
