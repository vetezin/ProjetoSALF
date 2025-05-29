package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.CotacaoController; // Importe o CotacaoController
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apis/cotacao") // Mapeamento para endpoints de cotação
public class CotacaoView {

    @Autowired
    private CotacaoController controller; // Injeta o CotacaoController

    // Endpoint para listar todas as cotações com filtro
    @GetMapping
    public ResponseEntity<Object> listar(@RequestParam(required = false) String filtro) {
        List<Map<String, Object>> cotacoes = controller.getCotacoes(filtro);
        return cotacoes == null || cotacoes.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma cotação encontrada."))
                : ResponseEntity.ok(cotacoes);
    }

    // Endpoint para retornar todas as cotações sem filtro
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> cotacoes = controller.getCotacoes(null);
        return cotacoes == null || cotacoes.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma cotação encontrada."))
                : ResponseEntity.ok(cotacoes);
    }

    // Endpoint para buscar cotação por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable int id) {
        Map<String, Object> resposta = controller.getCotacao(id);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(resposta);
    }

    // Endpoint para cadastrar uma nova cotação
    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarCotacao(@RequestBody Map<String, Object> dados) {
        // Extração dos dados do Map recebido
        String dataAbertura = (String) dados.get("cot_dtabertura");
        String dataFechamento = (String) dados.get("cot_dtfechamento"); // Pode ser null
        Integer lcCodInt = (Integer) dados.get("lc_cod");
        List<Integer> fornecedorCods = (List<Integer>) dados.get("fornecedorCods"); // Lista de IDs de fornecedores

        // Validação básica dos dados obrigatórios
        if (dataAbertura == null || dataAbertura.isBlank() || lcCodInt == null || lcCodInt <= 0) {
            return ResponseEntity.badRequest().body(new Mensagem("Dados de entrada incompletos ou inválidos para cadastrar a cotação."));
        }
        int lcCod = lcCodInt;

        Map<String, Object> json = controller.registrarCotacao(dataAbertura, dataFechamento, lcCod, fornecedorCods);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // Endpoint para alterar uma cotação existente
    @PutMapping // Sem path específico, o frontend chamará "/apis/cotacao"
    public ResponseEntity<Object> alterarCotacao(@RequestBody Map<String, Object> dados) {
        // Extrai os dados do Map recebido
        Integer cotCodInt = (Integer) dados.get("cot_cod"); // ID da cotação
        String dataAbertura = (String) dados.get("cot_dtabertura");
        String dataFechamento = (String) dados.get("cot_dtfechamento");
        Integer lcCodInt = (Integer) dados.get("lc_cod");
        List<Integer> novosFornecedorCods = (List<Integer>) dados.get("fornecedorCods"); // Nova lista de IDs de fornecedores

        // Validação básica
        if (cotCodInt == null || cotCodInt <= 0 || dataAbertura == null || dataAbertura.isBlank() || lcCodInt == null || lcCodInt <= 0) {
            return ResponseEntity.badRequest().body(new Mensagem("Dados de entrada incompletos ou inválidos para alterar a cotação."));
        }
        int cotCod = cotCodInt;
        int lcCod = lcCodInt;

        Map<String, Object> resposta = controller.updtCotacao(cotCod, dataAbertura, dataFechamento, lcCod, novosFornecedorCods);

        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem(resposta.get("mensagem").toString()));
    }

    // Endpoint para excluir uma cotação
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable int id) {
        Map<String, Object> resposta = controller.deletarCotacao(id);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem((String) resposta.get("erro")))
                : ResponseEntity.ok(new Mensagem((String) resposta.get("mensagem")));
    }
}