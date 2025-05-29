package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projetoSalf.mvc.Controller.FornecedorController;
import projetoSalf.mvc.util.Mensagem;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apis/fornecedor")
public class FornecedorView {

    @Autowired
    private FornecedorController fornecedorController;

    // Endpoint para listar todos os fornecedores, com a opção de um filtro
    @GetMapping
    public ResponseEntity<Object> listar(@RequestParam(required = false) String filtro) {
        List<Map<String, Object>> fornecedores = fornecedorController.getFornecedores(filtro);

        if (fornecedores == null || fornecedores.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(fornecedores);
        }
    }

    // Endpoint para retornar todos os fornecedores (alternativa para /apis/fornecedor sem filtro)
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        return listar(null); // Reutiliza o método 'listar', passando null para o filtro
    }

    // Endpoint para buscar um fornecedor específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable int id) {
        Map<String, Object> resposta = fornecedorController.getFornecedor(id);

        if (resposta.containsKey("erro")) {
            return ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()));
        } else {
            return ResponseEntity.ok(resposta);
        }
    }

    // Endpoint para cadastrar um novo fornecedor
    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarFornecedor(@RequestBody Map<String, Object> dados) {
        // Extrai os dados do mapa JSON recebido no corpo da requisição
        String nome = (String) dados.get("forn_nome");
        String end = (String) dados.get("forn_end"); // Alterado para forn_end
        String cnpj = (String) dados.get("forn_cnpj");
        String telefone = (String) dados.get("forn_telefone");

        // Chama o método registrarFornecedor do controller com os dados extraídos
        Map<String, Object> json = fornecedorController.registrarFornecedor(
                nome, end, cnpj, telefone); // Parâmetros ajustados

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // Endpoint para alterar um fornecedor existente
    @PutMapping
    public ResponseEntity<Object> alterarFornecedor(@RequestBody Map<String, Object> dados) {
        // Extrai os dados do mapa JSON, incluindo o ID do fornecedor
        Integer codInt = (Integer) dados.get("forn_cod"); // ID do fornecedor
        String nome = (String) dados.get("forn_nome");
        String end = (String) dados.get("forn_end"); // Alterado para forn_end
        String cnpj = (String) dados.get("forn_cnpj");
        String telefone = (String) dados.get("forn_telefone");

        // Validação básica: verifica se o ID é válido
        if (codInt == null || codInt <= 0) {
            return ResponseEntity.badRequest().body(new Mensagem("ID do fornecedor inválido para alteração."));
        }
        int cod = codInt; // Converte para int primitivo

        // Chama o método updtFornecedor do controller com os dados atualizados
        Map<String, Object> json = fornecedorController.updtFornecedor(
                cod, nome, end, cnpj, telefone); // Parâmetros ajustados

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // Endpoint para deletar um fornecedor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarFornecedor(@PathVariable int id) {
        Map<String, Object> json = fornecedorController.deletarFornecedor(id);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }
}