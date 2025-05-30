package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.PessoaCarente;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RestController
@RequestMapping("/apis/pessoa_carente")
public class PessoaCarenteRestController {

    // GET - Buscar pessoa carente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPessoaPorId(@PathVariable int id) {
        if (!SingletonDB.conectar()) {
            return ResponseEntity.status(500).body("❌ Erro ao conectar.");
        }

        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT * FROM pessoa_carente WHERE pc_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PessoaCarente pc = new PessoaCarente();
                pc.setPcCod(rs.getInt("pc_cod"));
                pc.setPcNome(rs.getString("pc_nome"));
                pc.setPcEndereco(rs.getString("pc_endereco"));
                pc.setPcTelefone(rs.getString("pc_telefone"));
                pc.setPcCpf(rs.getString("pc_cpf"));
                return ResponseEntity.ok(pc);
            } else {
                return ResponseEntity.status(404).body("❌ Pessoa não encontrada.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao buscar pessoa: " + e.getMessage());
        }
    }

    // POST - Cadastrar nova pessoa carente
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarPessoa(@RequestBody PessoaCarente pc) {
        if (!SingletonDB.conectar()) {
            return ResponseEntity.status(500).body("❌ Erro ao conectar.");
        }

        try {
            Conexao con = SingletonDB.getConexao();
            int novoCod = con.getMaxPK("pessoa_carente", "pc_cod") + 1;

            String sql = "INSERT INTO pessoa_carente (pc_cod, pc_nome, pc_endereco, pc_telefone, pc_cpf) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, novoCod);
            stmt.setString(2, pc.getPcNome());
            stmt.setString(3, pc.getPcEndereco());
            stmt.setString(4, pc.getPcTelefone());
            stmt.setString(5, pc.getPcCpf());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                return ResponseEntity.ok("✅ Pessoa cadastrada com sucesso!");
            } else {
                return ResponseEntity.status(500).body("❌ Erro ao salvar pessoa.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro ao salvar pessoa: " + e.getMessage());
        }
    }
}
