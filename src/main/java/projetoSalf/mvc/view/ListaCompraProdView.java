package projetoSalf.mvc.view;

import projetoSalf.mvc.controller.ListaCompraProdController;
import projetoSalf.mvc.util.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/lc_prod")
public class ListaCompraProdView {

    @Autowired
    private ListaCompraProdController listaCompraProdController;

    @PostMapping
    public ResponseEntity<Object> addItemLista(
            @RequestParam("lc_prod_qtd") int qtd,
            @RequestParam("lc_cod") int codLista,
            @RequestParam("prod_cod") int codProduto) {

        Map<String, Object> json = listaCompraProdController.addItemLista(qtd, codLista, codProduto);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Produto adicionado à lista com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @PutMapping
    public ResponseEntity<Object> updateItemLista(
            @RequestParam("lc_prod_qtd") int qtd,
            @RequestParam("lc_cod") int codLista,
            @RequestParam("prod_cod") int codProduto) {

        Map<String, Object> json = listaCompraProdController.updateItemLista(qtd, codLista, codProduto);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem("Produto atualizado na lista com sucesso!"))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> lista = listaCompraProdController.getItensLista();
        return (lista != null && !lista.isEmpty())
                ? ResponseEntity.ok(lista)
                : ResponseEntity.badRequest().body(new Mensagem("Nenhum item encontrado."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getByListaId(@PathVariable("id") int idLista) {
        List<Map<String, Object>> lista = listaCompraProdController.getItensPorLista(idLista);
        return (lista != null && !lista.isEmpty())
                ? ResponseEntity.ok(lista)
                : ResponseEntity.badRequest().body(new Mensagem("Nenhum item encontrado para essa lista."));
    }

    @DeleteMapping
    public ResponseEntity<Object> deletarItemLista(
            @RequestParam("lc_cod") int codLista,
            @RequestParam("prod_cod") int codProduto) {

        Map<String, Object> json = listaCompraProdController.deletarItemLista(codLista, codProduto);
        return json.get("erro") == null
                ? ResponseEntity.ok(new Mensagem(json.get("mensagem").toString()))
                : ResponseEntity.badRequest().body(new Mensagem(json.get("erro").toString()));
    }
}
