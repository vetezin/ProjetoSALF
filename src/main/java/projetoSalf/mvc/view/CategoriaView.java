package projetoSalf.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.Controller.CategoriaController;
import projetoSalf.mvc.model.Categoria;
import projetoSalf.mvc.util.Mensagem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/categoria")
public class CategoriaView {

    @Autowired
    private CategoriaController categoriaController;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Map<String, Object>> lista = categoriaController.getCat();

        if (lista != null && !lista.isEmpty()) {
            return ResponseEntity.ok(lista);
        } else {
            return ResponseEntity.badRequest().body(new Mensagem("Nenhuma categoria encontrada."));
        }
    }
}