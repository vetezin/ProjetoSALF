package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioDAO implements IDAO<Funcionario> {





    @Override
    public Funcionario gravar(Funcionario func) {
        String SQL = "INSERT INTO funcionario(func_nome, func_cpf, func_senha, func_email, func_login, func_nivel) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        SingletonDB.conectar();
        try (Connection conn = SingletonDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, func.getFuncNome());
            stmt.setString(2, func.getFuncCpf());
            stmt.setString(3, func.getFuncSenha());
            stmt.setString(4, func.getFuncEmail());
            stmt.setString(5, func.getFuncLogin());
            stmt.setInt(6, func.getFuncNivel());

            int linhasAfetadas = stmt.executeUpdate();
            return (linhasAfetadas > 0) ? func : null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object alterar(Funcionario entidade) {
        return null;
    }


    public Funcionario alterarNome(String nomeNovo, String email) {

        Funcionario func = new Funcionario();
        SingletonDB.conectar();
        if(nomeNovo != null && email != null || !email.contains("@")) {
            try{
                String SQL = "UPDATE funcionario SET func_nome = ? WHERE func_email = ?";
                Connection con = SingletonDB.getConexao();
                PreparedStatement stmt = con.prepareStatement(SQL);
                stmt.setString(1, nomeNovo);
                stmt.setString(2,email);

                if(stmt.executeUpdate() > 0){
                    System.out.println("Funcionario alterado com sucesso!");
                }
                else
                    System.out.println("Funcionario não encontrado!");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            return null;
        }

        return func;
    }

    @Override
    public boolean apagar(Funcionario func) {
        return false;
    }


    public boolean apagarNome( String email){


        SingletonDB.conectar();
        if( email != null){
            try{
                String SQL = "DELETE FROM funcionario WHERE func_email = ? ";
                Connection con = SingletonDB.getConexao();
                PreparedStatement stmt = con.prepareStatement(SQL);

                stmt.setString(1, email);

                if(stmt.executeUpdate() > 0)
                    return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    public Funcionario get(int id) {
        return null;
    }

    @Override
    public List<Funcionario> get(String filtro) {
        return List.of();
    }
}
