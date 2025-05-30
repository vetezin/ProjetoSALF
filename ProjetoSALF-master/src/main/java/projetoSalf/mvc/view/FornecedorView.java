package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projetoSalf.mvc.controller.FornecedorController;
import projetoSalf.mvc.util.Mensagem;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/fornecedor")
public class FornecedorView {

    @Autowired
    private FornecedorController fornecedorService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> lista = fornecedorService.getFornecedor();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum Fornecedor encontrado."));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addFornecedor(
            @RequestParam("forn_nome") String nome,
            @RequestParam("forn_logradouro") String logradouro,
            @RequestParam("forn_numero") String numero,
            @RequestParam("forn_cep") String cep,
            @RequestParam("forn_cidade") String cidade,
            @RequestParam("forn_complemento") String complemento,
            @RequestParam("forn_cnpj") String cnpj,
            @RequestParam("forn_telefone") String telefone,
            @RequestParam("forn_contato") String contato,
            @RequestParam("forn_email") String email
    ) {
        Map<String, Object> json = fornecedorService.addFornecedor(
                nome, logradouro, numero, cep, cidade, complemento, cnpj, telefone, contato, email);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Fornecedor cadastrado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updateFornecedor(
            @RequestParam("forn_cod") int cod,
            @RequestParam("forn_nome") String nome,
            @RequestParam("forn_logradouro") String logradouro,
            @RequestParam("forn_numero") String numero,
            @RequestParam(value = "forn_cep", required = false) String cep,
            @RequestParam("forn_cidade") String cidade,
            @RequestParam(value = "forn_complemento", required = false) String complemento,
            @RequestParam("forn_cnpj") String cnpj,
            @RequestParam(value = "forn_telefone", required = false) String telefone,
            @RequestParam(value = "forn_contato", required = false) String contato,
            @RequestParam(value = "forn_email", required = false) String email
    ) {
        Map<String, Object> json = fornecedorService.updateFornecedor(
                cod, nome, logradouro, numero, cep, cidade, complemento, cnpj, telefone, contato, email);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Fornecedor alterado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFornecedor(@PathVariable int id) {
        Map<String, Object> json = fornecedorService.deletarFornecedor(id);

        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Fornecedor deletado com sucesso!"));
        else
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
