package projetoSalf.mvc.controller;

import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.dao.FuncionarioDAO;
import projetoSalf.mvc.model.Funcionario;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apis/funcionario")
public class FuncionarioController {

    @GetMapping("/{id}")
    public Map<String, Object> buscarFuncionarioPorId(@PathVariable int id) {
        FuncionarioDAO dao = new FuncionarioDAO();
        Funcionario f = dao.buscarPorId(id);

        Map<String, Object> resposta = new HashMap<>();
        if (f != null) {
            resposta.put("func_cod", f.getFunc_cod());
            resposta.put("func_nome", f.getFunc_nome());
        } else {
            resposta.put("erro", "Funcionário não encontrado");
        }

        return resposta;
    }
}
