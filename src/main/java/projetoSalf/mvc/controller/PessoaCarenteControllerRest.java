package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.PessoaCarente;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.util.Conexao;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/apis/pessoa_carente")
public class PessoaCarenteControllerRest {

    // Buscar pessoa carente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPessoaPorId(@PathVariable("id") int id) {
        if (!SingletonDB.conectar()) return ResponseEntity.status(500).body("Erro ao conectar.");
        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT * FROM pessoa_carente WHERE pc_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PessoaCarente pc = new PessoaCarente();
                pc.setCod(rs.getInt("pc_cod"));
                pc.setNome(rs.getString("pc_nome"));
                pc.setCpf(rs.getString("pc_cpf"));
                pc.setEndereco(rs.getString("pc_endereco"));
                pc.setTelefone(rs.getString("pc_telefone"));
                return ResponseEntity.ok(pc);
            } else {
                return ResponseEntity.status(404).body("Pessoa não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar pessoa: " + e.getMessage());
        }
    }

    // Buscar pessoa carente por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Object> buscarPessoaPorCpf(@PathVariable("cpf") String cpf) {
        if (!SingletonDB.conectar()) return ResponseEntity.status(500).body("Erro ao conectar.");
        try {
            cpf = cpf.replaceAll("[^\\d]", ""); // remove pontuação
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT * FROM pessoa_carente WHERE REPLACE(REPLACE(REPLACE(pc_cpf, '.', ''), '-', ''), ' ', '') = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PessoaCarente pc = new PessoaCarente();
                pc.setCod(rs.getInt("pc_cod"));
                pc.setNome(rs.getString("pc_nome"));
                pc.setCpf(rs.getString("pc_cpf"));
                pc.setEndereco(rs.getString("pc_endereco"));
                pc.setTelefone(rs.getString("pc_telefone"));
                return ResponseEntity.ok(pc);
            } else {
                return ResponseEntity.status(404).body("Pessoa não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar pessoa: " + e.getMessage());
        }
    }

    // Cadastrar nova pessoa carente
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarPessoa(@RequestBody PessoaCarente pc) {
        if (!SingletonDB.conectar()) return ResponseEntity.status(500).body("Erro ao conectar.");
        try {
            Conexao con = SingletonDB.getConexao();
            int novoCod = con.getMaxPK("pessoa_carente", "pc_cod") + 1;

            String sql = "INSERT INTO pessoa_carente (pc_cod, pc_nome, pc_endereco, pc_telefone, pc_cpf) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, novoCod);
            stmt.setString(2, pc.getNome());
            stmt.setString(3, pc.getEndereco());
            stmt.setString(4, pc.getTelefone());
            stmt.setString(5, pc.getCpf());

            int linhas = stmt.executeUpdate();
            return linhas > 0 ? ResponseEntity.ok("✅ Pessoa cadastrada com sucesso!") :
                    ResponseEntity.status(500).body("❌ Erro ao salvar.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erro SQL: " + e.getMessage());
        }
    }

    // Listar todas as pessoas carentes
    @GetMapping("/listar")
    public ResponseEntity<Object> listarTodas() {
        if (!SingletonDB.conectar()) return ResponseEntity.status(500).body("Erro ao conectar.");
        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT * FROM pessoa_carente ORDER BY pc_cod";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            List<Map<String, Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> pessoa = new HashMap<>();
                pessoa.put("pc_cod", rs.getInt("pc_cod"));
                pessoa.put("pc_nome", rs.getString("pc_nome"));
                pessoa.put("pc_endereco", rs.getString("pc_endereco"));
                pessoa.put("pc_telefone", rs.getString("pc_telefone"));
                pessoa.put("pc_cpf", rs.getString("pc_cpf"));
                lista.add(pessoa);
            }
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao listar pessoas: " + e.getMessage());
        }
    }

    // Alterar pessoa carente
    @PostMapping("/alterar/{id}")
    public ResponseEntity<String> alterarPessoa(@PathVariable("id") int id, @RequestBody PessoaCarente pc) {
        if (!SingletonDB.conectar()) return ResponseEntity.status(500).body("Erro ao conectar.");
        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "UPDATE pessoa_carente SET pc_nome = ?, pc_endereco = ?, pc_telefone = ?, pc_cpf = ? WHERE pc_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, pc.getNome());
            stmt.setString(2, pc.getEndereco());
            stmt.setString(3, pc.getTelefone());
            stmt.setString(4, pc.getCpf());
            stmt.setInt(5, id);

            int linhas = stmt.executeUpdate();
            return linhas > 0 ? ResponseEntity.ok("✅ Pessoa atualizada com sucesso!") :
                    ResponseEntity.status(404).body("❌ Pessoa não encontrada.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erro SQL: " + e.getMessage());
        }
    }

    // Excluir pessoa carente
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirPessoa(@PathVariable("id") int id) {
        if (!SingletonDB.conectar()) return ResponseEntity.status(500).body("Erro ao conectar.");
        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "DELETE FROM pessoa_carente WHERE pc_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();
            return linhas > 0 ? ResponseEntity.ok("✅ Pessoa excluída com sucesso!") :
                    ResponseEntity.status(404).body("❌ Pessoa não encontrada.");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Erro SQL: " + e.getMessage());
        }
    }
}
