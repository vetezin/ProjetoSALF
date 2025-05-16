//package projetoSalf.mvc.view;

//import casoft.mvc.controller.ParametrizacaoController;
//import projetoSalf.mvc.util.Mensagem;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

//import java.util.List;
//import java.util.Map;
//@CrossOrigin
//@RestController
//@RequestMapping("apis/param")
//public class ParametrizacaoView {

//  @Autowired
//  private ParametrizacaoController paramController;


//    @GetMapping
//    public ResponseEntity<Object> getParam() {
//        List<Map<String,Object>> listParam;
//        listParam=paramController.getParam("");
//        if (listParam!=null)
//            return ResponseEntity.ok(listParam.getFirst());
//        else
//            return ResponseEntity.badRequest().body(new Mensagem("Necessário cadastrar a Empresa"));
//    }
//    @PostMapping
//    public ResponseEntity<Object> addParam(@RequestParam("nomeEmpresa") String nome,@RequestParam("cnpj") String cnpj,@RequestParam("endereco")String endereco,@RequestParam("telefone")String telefone,@RequestPart("file") MultipartFile file) {
//        Map<String,Object> json =paramController.addParam(nome,cnpj,endereco,telefone,file);
//        if(json.get("erro")==null)
//            return ResponseEntity.ok(new Mensagem("Empresa cadastrada com sucesso!"));
//        else
//            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
//    }

//    @PutMapping
//    public ResponseEntity<Object> updtParam(@RequestParam("nomeEmpresa") String nome,@RequestParam("cnpj") String cnpj,@RequestParam("endereco")String endereco,@RequestParam("telefone")String telefone,@RequestPart("file") MultipartFile file) {
//        Map<String,Object> json =paramController.updtParam(nome,cnpj,endereco,telefone,file);
//        if(json.get("erro")==null)
//            return ResponseEntity.ok(new Mensagem("Empresa alterada com sucesso!"));
//        else
//            return ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
//    }
//}
