package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.PessoaCarente;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PessoaCarenteDAO implements IDAO<PessoaCarente> {

    @Override
    public PessoaCarente gravar(PessoaCarente pessoaCarente) {
        String sql = """
        INSERT INTO pessoa_carente(pc_nome, pc_cpf, pc_telefone, pc_endereco, pc_cep)
        VALUES ('#1', '#2', '#3', '#4', '#5');
        """;
        sql = sql.replace("#1", pessoaCarente.getPcNome());
        sql = sql.replace("#2", pessoaCarente.getPcCpf());
        sql = sql.replace("#3", pessoaCarente.getPcTelefone());
        sql = sql.replace("#4", pessoaCarente.getPcEndereco());
        sql = sql.replace("#5", pessoaCarente.getPcCep());

        if (SingletonDB.getConexao().manipular(sql)) {
            return pessoaCarente;
        } else {
            System.out.println("Erro ao inserir pessoa carente: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public PessoaCarente alterar(PessoaCarente pessoaCarente) {
        String sql = """
        UPDATE pessoa_carente SET
        pc_nome = '#1',
        pc_cpf = '#2',
        pc_telefone = '#3',
        pc_endereco = '#4',
        pc_cep = '#5'
        WHERE pc_cod = #6;
        """;
        sql = sql.replace("#1", pessoaCarente.getPcNome());
        sql = sql.replace("#2", pessoaCarente.getPcCpf());
        sql = sql.replace("#3", pessoaCarente.getPcTelefone());
        sql = sql.replace("#4", pessoaCarente.getPcEndereco());
        sql = sql.replace("#5", pessoaCarente.getPcCep());
        sql = sql.replace("#6", String.valueOf(pessoaCarente.getPcCod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return pessoaCarente;
        } else {
            System.out.println("Erro ao alterar pessoa carente: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public PessoaCarente get(int id) {
        String sql = "SELECT * FROM pessoa_carente WHERE pc_cod = " + id;
        PessoaCarente pessoaCarente = null;

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                pessoaCarente = new PessoaCarente(
                        rs.getInt("pc_cod"),
                        rs.getString("pc_nome"),
                        rs.getString("pc_cpf"),
                        rs.getString("pc_telefone"),
                        rs.getString("pc_endereco"),
                        rs.getString("pc_cep")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar pessoa carente por ID: " + e.getMessage());
        }

        return pessoaCarente;
    }

    @Override
    public List<PessoaCarente> get(String filtro) {
        List<PessoaCarente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pessoa_carente";

        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        sql += " ORDER BY pc_nome";

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                PessoaCarente pc = new PessoaCarente(
                        rs.getInt("pc_cod"),
                        rs.getString("pc_nome"),
                        rs.getString("pc_cpf"),
                        rs.getString("pc_telefone"),
                        rs.getString("pc_endereco"),
                        rs.getString("pc_cep")
                );
                lista.add(pc);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pessoas carentes: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean apagar(PessoaCarente pessoaCarente) {
        if (pessoaCarente == null) return false;

        String sql = "DELETE FROM pessoa_carente WHERE pc_cod = " + pessoaCarente.getPcCod();
        return SingletonDB.getConexao().manipular(sql);
    }

    public boolean isEmpty() {
        String sql = "SELECT * FROM pessoa_carente";
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }
}