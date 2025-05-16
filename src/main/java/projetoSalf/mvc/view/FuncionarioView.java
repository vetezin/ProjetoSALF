package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.FuncionarioController;
import projetoSalf.mvc.model.Funcionario;

@CrossOrigin
@RestController
@RequestMapping("salf/funcionario")
public class FuncionarioView {

    @Autowired
    private FuncionarioController funcionarioController;



    @GetMapping
    public ResponseEntity<Object> getFuncionarioByEmail(@RequestParam String email) {
        Funcionario func = funcionarioController.login(email);
        if (func != null) {
            return ResponseEntity.ok().body(func);
        } else {
            return ResponseEntity.status(404).body("Funcionário não encontrado.");
        }
    }
}
