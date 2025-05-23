package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.SaidaController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/saida")
public class SaidaView {

    @Autowired
    private SaidaController saidaController;

    @PostMapping("/registrar")
    public ResponseEntity<Object> registrarSaidaComProduto(
            @RequestParam("codProduto") int codProduto,
            @RequestParam("quantidadeSaida") int quantidadeSaida,
            @RequestParam("codFuncionario") int codFuncionario,
            @RequestParam("dataSaida") String dataSaida,
            @RequestParam("motivo") String motivo
    ) {
        Map<String, Object> json = saidaController.registrarSaidaComProduto(
                codProduto,
                quantidadeSaida,
                codFuncionario,
                dataSaida,
                motivo
        );

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Saída registrada com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> lista = saidaController.getSaidas();
        if (lista != null && !lista.isEmpty())
            return ResponseEntity.ok(lista);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Nenhuma saída encontrada."));
    }

}
