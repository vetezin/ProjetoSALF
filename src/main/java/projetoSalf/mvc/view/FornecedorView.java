package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.controller.FornecedorController;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/fornecedor")
public class FornecedorView {

    @Autowired
    private FornecedorController controller;
    @Autowired
    private FornecedorController fornecedorController;

    @GetMapping
    public ResponseEntity<Object> getAll() {

        List<Map<String, Object>> lista = controller.getFornecedor();
        if(lista != null && !lista.isEmpty())
            return ResponseEntity.ok(lista);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum Fornecedor encontrado."));
    }

    @PostMapping
    public ResponseEntity<Object> addProduto(
            @RequestParam("forn_nome") String nome,
            @RequestParam("forn_end") String endereco,
            @RequestParam("forn_cnpj") String cnpj,
            @RequestParam("forn_telefone") String telefone
    ){
        Map<String, Object> json = fornecedorController.addFornecedor(nome, endereco, cnpj, telefone);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Fornecedor cadastrado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updateProduto(
            @RequestParam("forn_cod") int cod,
            @RequestParam("forn_nome") String nome,
            @RequestParam("forn_end") String endereco,
            @RequestParam("forn_cnpj") String cnpj,
            @RequestParam("forn_telefone") String telefone
    ){
        Map<String,Object> json = fornecedorController.updateFornecedor(cod, nome, endereco, cnpj, telefone);
        if(json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Produto alterado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable int id) {
        Map<String, Object> json = fornecedorController.deletarFornecedor(id);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Produto deletado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
