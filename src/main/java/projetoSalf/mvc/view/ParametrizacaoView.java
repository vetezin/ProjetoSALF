package projetoSalf.mvc.view;


import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.ParametrizacaoController;
import projetoSalf.mvc.model.Parametrizacao;

@CrossOrigin
@RestController
@RequestMapping("salf/param")
public class ParametrizacaoView {


    @Autowired
    ParametrizacaoController parametrizacaoController;


    @PostMapping("/salvar-alterar")
    public ResponseEntity<Object> saveOuAtt(@RequestBody Parametrizacao pa) {

        boolean inserido = parametrizacaoController.salvarOuAtualizar(pa);

        if(inserido == true){
            return ResponseEntity.ok().body("Salvo com sucesso!");
        }
        else {
            return ResponseEntity.ok().body("Atualizado com sucesso!");
        }
    }

    @GetMapping
    public ResponseEntity<Object> getParametrizacao(@RequestParam String email) {
        Parametrizacao pa = parametrizacaoController.get(email);

        if (pa != null) {
            return ResponseEntity.ok().body(pa); // Retorna objeto JSON
        } else {
            return ResponseEntity.status(404).body("Parametrização não encontrada.");
        }
    }


}
