package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.CompraController;
import projetoSalf.mvc.Controller.EntradaCompraController;
import projetoSalf.mvc.dao.CompraDAO;
import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.util.Conexao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/compra")
@CrossOrigin(origins = "*")
public class CompraView {

    @Autowired
    private CompraController controller;

    @Autowired
    private EntradaCompraController entradaController;

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

    @PostMapping("/desfazerEstoque")
    public ResponseEntity<Object> desfazerEntrada(@RequestBody Map<String, Object> dados) {
        Map<String, Object> resposta = entradaController.desfazerEstoque(dados);

        if (resposta.containsKey("erro")) {
            return ResponseEntity.badRequest().body(resposta);
        }

        return ResponseEntity.ok(resposta);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompraPorId(@PathVariable int id) {
        CompraDAO dao = new CompraDAO();
        Compra compra = dao.buscarPorId(id); // sem passar Conexao

        if (compra != null) {
            Map<String, Object> compraMap = new HashMap<>();
            compraMap.put("id", compra.getCoCod());
            compraMap.put("descricao", compra.getCoDescricao());
            compraMap.put("valorTotal", compra.getCoValorTotal());
            compraMap.put("data", compra.getCoDtCompra());
            compraMap.put("totalItens", compra.getCoTotalItens());
            compraMap.put("funcCod", compra.getFuncionarioFuncCod());
            compraMap.put("cot_forn_cotacao_cot_cod", compra.getCotFornCotacaoCotCod());
            compraMap.put("cot_forn_fornecedor_forn_cod", compra.getCotFornFornecedorFornCod());
            compraMap.put("confirmada", compra.isCoConfirmada());

            return ResponseEntity.ok(compraMap);
        } else {
            return ResponseEntity.status(404).body(Map.of("erro", "Compra não encontrada"));
        }
    }

    @DeleteMapping("/item")
    public Map<String, Object> excluirItem(@RequestBody Map<String, Object> dados) {
        return controller.excluirItemCompra(dados);
    }


}
