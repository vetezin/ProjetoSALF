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



    @PostMapping("/registrarMultiplos")
    public ResponseEntity<Object> registrarSaidaComProdutos(@RequestBody Map<String, Object> dados) {
        List<Map<String, Integer>> produtos = (List<Map<String, Integer>>) dados.get("produtos");
        int codFuncionario = (int) dados.get("codFuncionario");
        String dataSaida = (String) dados.get("dataSaida");
        String motivo = (String) dados.get("motivo");

        Map<String, Object> json = saidaController.registrarSaidaComProdutos(produtos, codFuncionario, dataSaida, motivo);

        if (json.get("erro") == null)
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }





    @GetMapping("/listar-com-produtos")
    public ResponseEntity<Object> getSaidasComProdutos() {
        List<Map<String, Object>> resultado = saidaController.getSaidasComProdutos();
        if (resultado == null || resultado.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("erro", "Nenhuma saída encontrada"));
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> deletarSaidaComProdutos(@PathVariable int id) {
        Map<String, Object> resultado = saidaController.deletarSaidaComProdutos(id);

        if (resultado.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(resultado.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        }
    }

}
