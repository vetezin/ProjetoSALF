package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;

import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FuncionarioDAO implements IDAO<Funcionario> {

    @Override
    public Funcionario gravar(Funcionario funcionario) {
        String sql = """
            INSERT INTO funcionario(func_nome, func_cpf, func_senha, func_email, func_login, func_nivel)
            VALUES ('#1', '#2', '#3', '#4', '#5', #6);
        """;
        sql = sql.replace("#1", funcionario.getNome())
                .replace("#2", funcionario.getCpf())
                .replace("#3", funcionario.getSenha())
                .replace("#4", funcionario.getEmail())
                .replace("#5", funcionario.getLogin())
                .replace("#6", String.valueOf(funcionario.getNivel()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return funcionario;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Funcionario alterar(Funcionario funcionario) {
        String sql = """
            UPDATE funcionario SET 
                func_nome = '#1',
                func_cpf = '#2',
                func_senha = '#3',
                func_email = '#4',
                func_login = '#5',
                func_nivel = #6
            WHERE func_cod = #7;
        """;
        sql = sql.replace("#1", funcionario.getNome())
                .replace("#2", funcionario.getCpf())
                .replace("#3", funcionario.getSenha())
                .replace("#4", funcionario.getEmail())
                .replace("#5", funcionario.getLogin())
                .replace("#6", String.valueOf(funcionario.getNivel()))
                .replace("#7", String.valueOf(funcionario.getId()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return funcionario;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Funcionario func) {
        if (func == null)
            return false;

        String sql = "DELETE FROM funcionario WHERE func_cod = " + func.getId();
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public Funcionario get(int id) {
        String sql = "SELECT * FROM funcionario WHERE func_cod = " + id;
        var rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                Funcionario func = new Funcionario();
                func.setFunc_cod(rs.getInt("func_cod"));
                func.setFunc_nome(rs.getString("func_nome"));
                func.setFunc_cpf(rs.getString("func_cpf"));
                func.setFunc_senha(rs.getString("func_senha"));
                func.setFunc_email(rs.getString("func_email"));
                func.setFunc_login(rs.getString("func_login"));
                func.setFunc_nivel(rs.getInt("func_nivel"));
                return func;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar funcionário: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Funcionario> get(String filtro) {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Funcionario func = new Funcionario();
                func.setFunc_cod(rs.getInt("func_cod"));
                func.setFunc_nome(rs.getString("func_nome"));
                func.setFunc_cpf(rs.getString("func_cpf"));
                func.setFunc_senha(rs.getString("func_senha"));
                func.setFunc_email(rs.getString("func_email"));
                func.setFunc_login(rs.getString("func_login"));
                func.setFunc_nivel(rs.getInt("func_nivel"));
                lista.add(func);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar funcionários: " + e.getMessage());
        }
        return lista;
    }
}