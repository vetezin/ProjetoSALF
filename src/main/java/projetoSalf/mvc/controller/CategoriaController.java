package projetoSalf.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.dao.CategoriaProdutoDAO;
import projetoSalf.mvc.model.CategoriaProduto;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/categorias")

public class CategoriaController {

    @Autowired
    private CategoriaProdutoDAO dao;

    @GetMapping
    public List<CategoriaProduto> listar() {
        return dao.consultarTodos();
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
    @PutMapping
    public ResponseEntity<?> atualizar(@RequestBody CategoriaProduto categoria) {
        dao.atualizar(categoria);
        return ResponseEntity.ok().build();
    }

}
