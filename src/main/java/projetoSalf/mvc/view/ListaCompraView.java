package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.ListaCompraController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/apis/lista")
public class ListaCompraView {

    @Autowired
    private ListaCompraController controller;

    // Endpoint para listar todas as listas de compras com filtro
    @GetMapping
    public ResponseEntity<Object> listar(@RequestParam(required = false) String filtro) {
        List<Map<String, Object>> listas = controller.getListasCompra(filtro);
        return listas == null || listas.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma lista encontrada."))
                : ResponseEntity.ok(listas);
    }

    // Endpoint para retornar todas as listas de compras sem filtro
    @GetMapping("/all")
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> listas = controller.getListasCompra(null);
        return listas == null || listas.isEmpty()
                ? ResponseEntity.badRequest().body(new Mensagem("Nenhuma lista encontrada."))
                : ResponseEntity.ok(listas);
    }

    // Endpoint para buscar lista de compras por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscar(@PathVariable int id) {
        Map<String, Object> resposta = controller.getListaCompra(id);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(resposta);
    }

    // --- CADASTRO: Agora usa @RequestBody para um objeto JSON completo ---
    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarListaCompra(@RequestBody Map<String, Object> dados) {
        // A extração de dados do 'dados' Map é feita aqui na View, similar ao SaidaView
        // Certifique-se de que o frontend envie o JSON com as chaves esperadas:
        // "lc_dtlista", "lc_desc", "func_cod", e "produtos" (que é uma lista de mapas)

        String dataCriacao = (String) dados.get("lc_dtlista"); // Nome da coluna no banco e chave no JSON
        String descricao = (String) dados.get("lc_desc");     // Nome da coluna no banco e chave no JSON
        Integer codFuncionarioInt = (Integer) dados.get("func_cod"); // Nome da coluna no banco e chave no JSON
        List<Map<String, Integer>> produtos = (List<Map<String, Integer>>) dados.get("produtos");

        // Validação básica para evitar NullPointerException ou erros de cast
        if (dataCriacao == null || descricao == null || codFuncionarioInt == null || produtos == null) {
            return ResponseEntity.badRequest().body(new Mensagem("Dados de entrada incompletos ou inválidos para cadastrar a lista."));
        }
        int codFuncionario = codFuncionarioInt; // Converte para int primitivo

        Map<String, Object> json = controller.registrarListaCompra(produtos, codFuncionario, dataCriacao, descricao);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(json);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }

    // --- ALTERAR: Agora usa @RequestBody para um objeto JSON completo ---
    // Remove os @RequestParam e usa @RequestBody para um Map
    @PutMapping // Sem path específico, o frontend chamará "/apis/lista"
    public ResponseEntity<Object> alterarListaCompra(@RequestBody Map<String, Object> dados) {
        // Extrai os dados do Map recebido
        Integer idInt = (Integer) dados.get("lc_cod"); // ID da lista, nome da coluna no banco
        String descricao = (String) dados.get("lc_desc");
        String dataCriacao = (String) dados.get("lc_dtlista");
        Integer funcionarioIdInt = (Integer) dados.get("func_cod"); // ID do funcionário

        // Validação básica
        if (idInt == null || descricao == null || dataCriacao == null || funcionarioIdInt == null) {
            return ResponseEntity.badRequest().body(new Mensagem("Dados de entrada incompletos ou inválidos para alterar a lista."));
        }
        int id = idInt;
        int funcionarioId = funcionarioIdInt;

        Map<String, Object> resposta = controller.updtListaCompra(id, descricao, dataCriacao, funcionarioId);

        // CORREÇÃO AQUI: O controller retorna "mensagem" em caso de sucesso.
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem(resposta.get("erro").toString()))
                : ResponseEntity.ok(new Mensagem(resposta.get("mensagem").toString())); // Alterado de "sucesso" para "mensagem"
    }

    // Endpoint para excluir uma lista de compras
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable int id) { // Mudado para @PathVariable
        // Removido @RequestBody do ID, pois DELETE com ID na URL não deve ter body
        Map<String, Object> resposta = controller.deletarListaCompra(id);
        return resposta.containsKey("erro")
                ? ResponseEntity.badRequest().body(new Mensagem((String) resposta.get("erro")))
                : ResponseEntity.ok(new Mensagem((String) resposta.get("mensagem"))); // Alterado de "sucesso" para "mensagem"
    }
}