package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/apis/necessidade")
public class NecessidadePCController {

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarNecessidade(@RequestBody Map<String, Object> dados) {
        try {
            if (!SingletonDB.conectar()) {
                return ResponseEntity.status(500).body("❌ Falha ao conectar com o banco de dados.");
            }

            Conexao con = SingletonDB.getConexao();

            String npc_desc = (String) dados.get("npc_desc");
            int func_cod = (int) dados.get("func_cod");
            int pc_cod = (int) dados.get("pc_cod");

            String sql = "INSERT INTO necessidade_pc (npc_desc, npc_dtcriacao, func_cod, pc_cod) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, npc_desc);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setInt(3, func_cod);
            stmt.setInt(4, pc_cod);

            stmt.execute();

            return ResponseEntity.ok("✅ Necessidade registrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao registrar necessidade: " + e.getMessage());
        }
    }
}
