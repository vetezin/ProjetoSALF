package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetoSalf.mvc.Controller.ParametrizacaoController;
import projetoSalf.mvc.model.Parametrizacao;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("salf/param")
public class ParametrizacaoView {

    @Autowired
    private ParametrizacaoController parametrizacaoController;

    @PostMapping( consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> salvarOuAtualizar(
            @RequestParam("nomeEmpresa") String nomeEmpresa,
            @RequestParam("cnpj") String cnpj,
            @RequestParam("endereco") String endereco,
            @RequestParam("telefone") String telefone,
            @RequestParam("email") String email,
            @RequestParam("logotipo") MultipartFile logotipo) {

        try {
            Parametrizacao pa = new Parametrizacao();
            pa.setNomeEmpresa(nomeEmpresa);
            pa.setCnpj(cnpj);
            pa.setEndereco(endereco);
            pa.setTelefone(telefone);
            pa.setEmail(email);

            // Converte arquivo para array de bytes e seta no objeto
            pa.setLogotipo(logotipo.getBytes());

            // Salvar no banco
            parametrizacaoController.salvarOuAtualizar(pa);

            return ResponseEntity.ok("Empresa cadastrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar empresa: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<Object> getParametrizacao(@RequestParam String email) {
        Parametrizacao pa = parametrizacaoController.get(email);

        if (pa != null) {
            return ResponseEntity.ok(pa);
        } else {
            return ResponseEntity.status(404).body("Parametrização não encontrada.");
        }
    }

    @GetMapping("/existeEmpresa")
    public ResponseEntity<Boolean> existeEmpresa() {
        boolean existe = parametrizacaoController.listar();
        return ResponseEntity.ok(existe);
    }

}
