package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.EntradaCompraController;
import projetoSalf.mvc.model.Compra;

import java.util.Map;

@RestController
@RequestMapping("/apis/entrada")
@CrossOrigin(origins = "*")
public class EntradaCompraView {

    @Autowired
    private EntradaCompraController controller;

    @PostMapping("/registrar")
    public ResponseEntity<Object> registrar(@RequestBody Compra compra) {
        Map<String, Object> resposta = controller.registrarEntradaCompra(compra);
        if (resposta.containsKey("erro")) {
            return ResponseEntity.badRequest().body(resposta);
        }
        return ResponseEntity.ok(resposta);
    }
}
