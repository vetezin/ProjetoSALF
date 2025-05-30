package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.AcertoEstoqueController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/acertoestoque")
public class AcertoEstoqueView {

    @Autowired
    private AcertoEstoqueController acertoController;

    // Registra um acerto de estoque para um produto
    @PostMapping("/registrar")
    public ResponseEntity<Object> registrarAcerto(
            @RequestParam("codProduto") int codProduto,
            @RequestParam("novaQuantidade") int novaQuantidade,
            @RequestParam("codFuncionario") int codFuncionario,
            @RequestParam("dataAcerto") String dataAcerto,
            @RequestParam("motivo") String motivo
    ) {
        Map<String, Object> resultado = acertoController.registrarAcertoProduto(
                codProduto, novaQuantidade, codFuncionario, dataAcerto, motivo
        );

        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        } else {
            return ResponseEntity.ok(resultado);
        }
    }

    // Lista todos os acertos registrados
    @GetMapping("listar-com-produtos")
    public ResponseEntity<Object> getAllAcertos() {
        List<Map<String, Object>> lista = acertoController.getAcertos();
        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum acerto encontrado."));
        }
    }

    // Deleta um acerto pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarAcerto(@PathVariable int id) {
        Map<String, Object> resultado = acertoController.deletarAcerto(id);

        if (resultado.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resultado.get("erro").toString()));
        } else {
            return ResponseEntity.ok(new Mensagem(resultado.get("mensagem").toString()));
        }
    }



}
