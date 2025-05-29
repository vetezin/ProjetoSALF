package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.DoaProd;
import projetoSalf.mvc.Controller.DoacaoController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/doacoes")
public class DoacaoView {

    @Autowired
    private DoacaoController doacaoController;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodas() {
        List<Map<String, Object>> lista = doacaoController.listarTodasDoacoes(null, null);
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/data/{data}")
    public ResponseEntity<List<Map<String, Object>>> listarPorData(@PathVariable String data) {
        List<Map<String, Object>> lista = doacaoController.listarTodasDoacoes(null, data);
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/funcionario/{funcCod}")
    public ResponseEntity<List<Map<String, Object>>> listarPorFuncionario(@PathVariable int funcCod) {
        List<Map<String, Object>> lista = doacaoController.listarTodasDoacoes(funcCod, null);
        if (lista.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<String> registrar(@RequestBody Map<String, Object> payload) {
        try {
            // Corrigido: pegar como Number para evitar ClassCastException
            Number funcCodNumber = (Number) payload.get("funcCod");
            if (funcCodNumber == null) {
                return ResponseEntity.status(400).body("Erro: campo 'funcCod' é obrigatório.");
            }
            int funcCod = funcCodNumber.intValue();

            List<Map<String, Object>> produtosData = (List<Map<String, Object>>) payload.get("produtos");

            if (produtosData == null || produtosData.isEmpty()) {
                return ResponseEntity.status(400).body("Erro: lista de produtos está vazia ou ausente.");
            }

            List<DoaProd> produtos = new ArrayList<>();
            for (Map<String, Object> p : produtosData) {
                DoaProd dp = new DoaProd();
                dp.setDoaProdQtd(((Number) p.get("doaProdQtd")).intValue());
                dp.setDoaProdCatCod(((Number) p.get("doaProdCatCod")).intValue());
                dp.setProdutoProdCod(((Number) p.get("produtoProdCod")).intValue());
                produtos.add(dp);
            }

            boolean sucesso = doacaoController.registrarDoacaoCompleta(funcCod, produtos);
            if (sucesso)
                return ResponseEntity.ok("Doação registrada com sucesso!");
            else
                return ResponseEntity.status(500).body("Erro ao registrar a doação.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{doaCod}")
    public ResponseEntity<String> deletar(@PathVariable int doaCod) {
        boolean sucesso = doacaoController.deletarDoacao(doaCod);
        if (sucesso)
            return ResponseEntity.ok("Doação deletada com sucesso e estoque ajustado.");
        else
            return ResponseEntity.status(500).body("Erro ao deletar a doação.");
    }

}
