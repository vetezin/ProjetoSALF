package projetoSalf.mvc.view;

import projetoSalf.mvc.controller.FuncionarioController;
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

    @GetMapping
    public ResponseEntity<Object> getFuncionario() {
        List<Map<String, Object>> listFunc = this.funcionarioController.getFuncionario();
        return listFunc != null && !listFunc.isEmpty()
                ? ResponseEntity.ok(listFunc.getFirst())
                : ResponseEntity.badRequest().body(new Mensagem("Nenhum funcionário cadastrado"));
    }

    @PostMapping
    public ResponseEntity<Object> addFuncionario(
            @RequestParam("func_nome") String nome,
            @RequestParam("func_cpf") String cpf,
            @RequestParam("func_senha") String senha,
            @RequestParam("func_email") String email,
            @RequestParam("func_login") String login,
            @RequestParam("func_nivel") int nivel,
            @RequestPart("file") MultipartFile file) {

        Map<String, Object> json = this.funcionarioController.addFuncionario(nome, cpf, senha, email, login, nivel, file);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Funcionário cadastrado com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updtFuncionario(
            @RequestParam("func_nome") String nome,
            @RequestParam("func_cpf") String cpf,
            @RequestParam("func_senha") String senha,
            @RequestParam("func_email") String email,
            @RequestParam("func_login") String login,
            @RequestParam("func_nivel") int nivel,
            @RequestPart("file") MultipartFile file) {

        Map<String, Object> json = this.funcionarioController.updtFuncionario(nome, cpf, senha, email, login, nivel, file);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Funcionário alterado com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
