package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projetoSalf.mvc.controller.ProdutoController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

public class ProdutoView {
////////
    @Autowired
    private ProdutoController produtoController;

    @GetMapping
    public ResponseEntity<Object> getProdutos() {
        List<Map<String, Object>> lista = produtoController.getProd();
        if (lista != null && !lista.isEmpty())
            return ResponseEntity.ok(lista);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum produto encontrado."));
    }

    @PostMapping
    public ResponseEntity<Object> addProduto(
            @RequestParam("prod_desc") String descricao,
            @RequestParam("prod_dtvalid") String validade,
            @RequestParam("prod_valorun") float valor,
            @RequestParam("cat_cod") int catCod
    ) {
        Map<String, Object> json = produtoController.addProduto(descricao, validade, valor, catCod);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Produto cadastrado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updtProduto(
            @RequestParam("prod_cod") long cod,
            @RequestParam("prod_desc") String descricao,
            @RequestParam("prod_dtvalid") String validade,
            @RequestParam("prod_valorun") float valor,
            @RequestParam("cat_cod") int catCod
    ) {
        Map<String, Object> json = produtoController.updtProduto(cod, descricao, validade, valor, catCod);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Produto alterado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
