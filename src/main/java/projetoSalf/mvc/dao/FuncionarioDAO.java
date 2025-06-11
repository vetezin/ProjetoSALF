package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FuncionarioDAO {

    public Funcionario buscarPorId(int id) {
        try {
            Conexao con = SingletonDB.getConexao();
            String sql = "SELECT func_cod, func_nome FROM funcionario WHERE func_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Funcionario f = new Funcionario();
                f.setFunc_cod(rs.getInt("func_cod"));
                f.setFunc_nome(rs.getString("func_nome"));
                return f;
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Erro ao buscar funcionário por ID: " + e.getMessage());
        }
        return null;
    }
}

