package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apis/produto")
public class ProdutoControllerRest {

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarProduto(@PathVariable("id") int id) {
        if (!SingletonDB.conectar()) {
            return ResponseEntity.status(500).body("Erro ao conectar.");
        }

        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT prod_desc FROM produto WHERE prod_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Map<String, Object> produto = new HashMap<>();
                produto.put("descricao", rs.getString("prod_desc"));
                return ResponseEntity.ok(produto);
            } else {
                return ResponseEntity.status(404).body("Produto não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }
}
