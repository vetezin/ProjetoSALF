package projetoSalf.mvc.view;

import projetoSalf.mvc.Controller.FuncionarioController;
import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping({"apis/funcionario"})
public class FuncionarioView {

    @Autowired
    private FuncionarioController funcionarioController;

    @PostMapping
    public ResponseEntity<Object> addFuncionario(
            @RequestParam("func_nome") String nome,
            @RequestParam("func_cpf") String cpf,
            @RequestParam("func_senha") String senha,
            @RequestParam("func_email") String email,
            @RequestParam("func_login") String login,
            @RequestParam("func_nivel") int nivel) {

        Map<String, Object> json = this.funcionarioController.addFuncionario(nome, cpf, senha, email, login, nivel);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Funcionário cadastrado com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updtFuncionario(
            @RequestParam("func_cod") int cod,
            @RequestParam("func_nome") String nome,
            @RequestParam("func_cpf") String cpf,
            @RequestParam("func_senha") String senha,
            @RequestParam("func_email") String email,
            @RequestParam("func_login") String login,
            @RequestParam("func_nivel") int nivel) {

        Map<String, Object> json = this.funcionarioController.updtFuncionario(cod, nome, cpf, senha, email, login, nivel);
        if (json.get("erro") == null)
            return ResponseEntity.ok(new Mensagem("Funcionario alterado com sucesso!"));
        return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
    @GetMapping
    public ResponseEntity<Object> getAll() {

        List<Map<String, Object>> lista = funcionarioController.getFuncionario();
        if (lista != null && !lista.isEmpty())
            return ResponseEntity.ok(lista);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum funcionario encontrado."));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFuncId(@RequestParam("func_cod") int cod) {

        Map<String, Object> json = funcionarioController.getFuncionario(cod);
        if (json != null)
            return ResponseEntity.ok(json);
        else
            return ResponseEntity.badRequest().body(new Mensagem("Nenhum funcionario encontrado."));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarFuncionario(@PathVariable("id") int id) {
        Map<String, Object> json = funcionarioController.deletarFuncionario(id);

        if (json.get("erro") == null) {
            return ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()));
        } else {
            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
        }
    }
}