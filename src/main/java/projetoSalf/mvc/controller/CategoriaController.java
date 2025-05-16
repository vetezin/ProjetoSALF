package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.dao.CategoriaProdutoDAO;
import projetoSalf.mvc.model.CategoriaProduto;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaProdutoDAO dao;

    @GetMapping
    public List<CategoriaProduto> listar() {
        return dao.listar();
    }

    @PostMapping
    public ResponseEntity<?> inserir(@RequestBody CategoriaProduto categoria) {
        dao.inserir(categoria);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        dao.excluir(id);
        return ResponseEntity.ok().build();
    }
}
