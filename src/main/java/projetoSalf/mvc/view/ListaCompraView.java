package projetoSalf.mvc.view;

import projetoSalf.mvc.controller.ListaCompraController;
import projetoSalf.mvc.model.ListaCompra;
import projetoSalf.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/listacompra")
public class ListaCompraView {

    @Autowired
    private ListaCompraController listaCompraController;

    @PostMapping
    public ResponseEntity<Object> addListaCompra(
            @RequestParam("lc_dtlista") String data,
            @RequestParam("lc_desc") String descricao,
            @RequestParam("func_cod") int funcCod) {

        Map<String, Object> json = listaCompraController.addListaCompra(data, descricao, funcCod);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Lista de compra cadastrada com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updateListaCompra(
            @RequestParam("lc_cod") int cod,
            @RequestParam("lc_dtlista") String data,
            @RequestParam("lc_desc") String descricao,
            @RequestParam("func_cod") int funcCod) {

        Map<String, Object> json = listaCompraController.updateListaCompra(cod, data, descricao, funcCod);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Lista de compra atualizada com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> lista = listaCompraController.getListasCompra();
        return (lista != null && !lista.isEmpty())
                ? ResponseEntity.ok(lista)
                : ResponseEntity.badRequest().body(new Mensagem("Nenhuma lista de compra encontrada."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") int id) {
        Map<String, Object> json = listaCompraController.getListaCompra(id);
        return json != null
                ? ResponseEntity.ok(json)
                : ResponseEntity.badRequest().body(new Mensagem("Lista de compra não encontrada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarListaCompra(@PathVariable("id") int id) {
        Map<String, Object> json = listaCompraController.deletarListaCompra(id);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
