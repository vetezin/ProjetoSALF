package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.CategoriaProduto;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/apis/categoria")
public class CategoriaProdutoController {

    // GET - Listar todas as categorias
    @GetMapping
    public ResponseEntity<List<CategoriaProduto>> listar() {
        List<CategoriaProduto> lista = new ArrayList<>();

        if (!SingletonDB.conectar()) {
            return ResponseEntity.internalServerError().build();
        }

        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT * FROM categoria_produto ORDER BY cat_cod";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriaProduto c = new CategoriaProduto();
                c.setId(rs.getInt("cat_cod"));
                c.setDesc(rs.getString("cat_desc"));
                lista.add(c);
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST - Cadastrar nova categoria
    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody CategoriaProduto categoria) {
        if (!SingletonDB.conectar()) {
            return ResponseEntity.status(500).body("Erro ao conectar ao banco.");
        }

        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "INSERT INTO categoria_produto (cat_desc) VALUES (?)";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, categoria.getDesc());
            stmt.executeUpdate();

            return ResponseEntity.ok("✅ Categoria cadastrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao cadastrar categoria.");
        }
    }

    // PUT - Atualizar categoria existente
    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody CategoriaProduto categoria) {
        if (!SingletonDB.conectar()) {
            return ResponseEntity.status(500).body("Erro ao conectar ao banco.");
        }

        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "UPDATE categoria_produto SET cat_desc = ? WHERE cat_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, categoria.getDesc());
            stmt.setInt(2, categoria.getId());
            stmt.executeUpdate();

            return ResponseEntity.ok("✅ Categoria atualizada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao atualizar categoria.");
        }
    }

    // DELETE - Remover categoria pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        if (!SingletonDB.conectar()) {
            return ResponseEntity.status(500).body("Erro ao conectar.");
        }

        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "DELETE FROM categoria_produto WHERE cat_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            return ResponseEntity.ok("✅ Categoria excluída com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao excluir categoria.");
        }
    }
}
