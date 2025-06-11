package projetoSalf.mvc.controller;

import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/apis/necessidade")
public class NecessidadePCController {

    @PostMapping("/registrar")
    public String registrarNecessidade(@RequestBody Map<String, Object> dados) {
        try {
            if (!SingletonDB.conectar()) return "❌ Falha ao conectar com o banco.";
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
            return "✅ Necessidade registrada com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erro ao registrar necessidade: " + e.getMessage();
        }
    }

    @GetMapping("/buscarPorCpf")
    public List<Map<String, Object>> buscarPorCpf(@RequestParam String cpf) {
        List<Map<String, Object>> lista = new ArrayList<>();
        try {
            if (!SingletonDB.conectar()) return lista;
            cpf = cpf.replaceAll("[^\\d]", ""); // limpa o CPF

            Conexao con = SingletonDB.getConexao();
            String sql = """
                SELECT npc.npc_cod, npc.npc_desc, npc.func_cod, npc.pc_cod, npc.npc_dtcriacao
                FROM necessidade_pc npc
                JOIN pessoa_carente pc ON npc.pc_cod = pc.pc_cod
                WHERE REPLACE(REPLACE(REPLACE(pc.pc_cpf, '.', ''), '-', ''), ' ', '') = ?
            """;
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> linha = new HashMap<>();
                linha.put("npc_cod", rs.getInt("npc_cod"));
                linha.put("npc_desc", rs.getString("npc_desc"));
                linha.put("func_cod", rs.getInt("func_cod"));
                linha.put("pc_cod", rs.getInt("pc_cod"));
                linha.put("npc_dtcriacao", rs.getDate("npc_dtcriacao").toString());
                lista.add(linha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @PutMapping("/{npc_cod}")
    public String alterarNecessidade(@PathVariable int npc_cod, @RequestBody Map<String, Object> dados) {
        try {
            if (!SingletonDB.conectar()) return "❌ Falha ao conectar com o banco.";
            Conexao con = SingletonDB.getConexao();
            String sql = "UPDATE necessidade_pc SET npc_desc = ?, func_cod = ?, pc_cod = ? WHERE npc_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, (String) dados.get("npc_desc"));
            stmt.setInt(2, (int) dados.get("func_cod"));
            stmt.setInt(3, (int) dados.get("pc_cod"));
            stmt.setInt(4, npc_cod);

            stmt.executeUpdate();
            return "✅ Necessidade alterada com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erro ao alterar necessidade: " + e.getMessage();
        }
    }

    @DeleteMapping("/{npc_cod}")
    public String excluirNecessidade(@PathVariable int npc_cod) {
        try {
            if (!SingletonDB.conectar()) return "❌ Falha ao conectar com o banco.";
            Conexao con = SingletonDB.getConexao();
            String sql = "DELETE FROM necessidade_pc WHERE npc_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, npc_cod);

            stmt.executeUpdate();
            return "✅ Necessidade excluída com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erro ao excluir necessidade: " + e.getMessage();
        }
    }
}
