package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.DoacaoPCController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apis/doacao_pc")
public class DoacaoPCView {

    @Autowired
    private DoacaoPCController controller;

    @PostMapping("/registrar")
    public Map<String, Object> registrar(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            response = controller.registrarDoacaoPC(body);
        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Exceção: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @GetMapping("/listar")
    public List<Map<String, Object>> listarTodos() {
        return controller.listarTodos();
    }

    @GetMapping("/{id}")
    public Map<String, Object> buscarPorId(@PathVariable int id) {
        return controller.buscarPorId(id);
    }

    @GetMapping("/funcionario/{funcCod}")
    public List<Map<String, Object>> listarPorFuncionario(@PathVariable int funcCod) {
        return controller.listarPorFuncionario(funcCod);
    }

    @GetMapping("/pessoa_carente/{pcCod}")
    public List<Map<String, Object>> listarPorPessoaCarente(@PathVariable int pcCod) {
        return controller.listarPorPessoaCarente(pcCod);
    }

    @DeleteMapping("/excluir/{doapcCod}")
    public Map<String, Object> deletar(@PathVariable int doapcCod) {
        return controller.deletar(doapcCod);
    }
}
