package projetoSalf.mvc.dao;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
public class FuncionarioDAO implements IDAO<Funcionario> {





    @Override
    public Funcionario gravar(Funcionario func) {
        String SQL = "INSERT INTO funcionario(func_nome, func_cpf, func_senha, func_email, func_login, func_nivel) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Conexao conexao = new Conexao();
            Connection conn = conexao.getConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL);

            stmt.setString(1, func.getFuncNome());
            stmt.setString(2, func.getFuncCpf());
            stmt.setString(3, func.getFuncSenha());
            stmt.setString(4, func.getFuncEmail());
            stmt.setString(5, func.getFuncLogin());
            stmt.setInt(6, func.getFuncNivel());

            if(stmt.execute()){
                conexao.fechar();
                return func;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public Funcionario alterar(Funcionario func) {

        String SQL = "UPDATE funcionario SET func_nome = ? WHERE func_email = ?";

        try{
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setString(1, func.getFuncNome());
            stmt.setString(2, func.getFuncEmail());

            if(stmt.executeUpdate() > 0){
                conexao.fechar();
                return func;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public boolean apagar(Funcionario func) {

        String SQL = "DELETE FROM funcionario WHERE func_email = ?";

        try{

            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setString(1, func.getFuncEmail());

            if(stmt.executeUpdate() > 0){
                conexao.fechar();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }



    @Override
    public Funcionario get(int id) {

        String SQL = "SELECT * FROM funcionario WHERE func_cod = ?";
        Funcionario func = new Funcionario();

        try{

            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){

                func.setFuncNome(rs.getString("func_nome"));
                func.setFuncCpf(rs.getString("func_cpf"));
                func.setFuncSenha(rs.getString("func_senha"));
                func.setFuncEmail(rs.getString("func_email"));
                func.setFuncLogin(rs.getString("func_login"));
                func.setFuncNivel(rs.getInt("func_nivel"));

                conexao.fechar();
                return func;

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }




        return func;
    }


    public Funcionario buscarPorEmail(String email) {
        String SQL = "SELECT * FROM funcionario WHERE func_email = ? ";
        Funcionario func = null;

        try {
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                func = new Funcionario();
                func.setFuncNome(rs.getString("func_nome"));
                func.setFuncCpf(rs.getString("func_cpf"));
                func.setFuncSenha(rs.getString("func_senha"));
                func.setFuncEmail(rs.getString("func_email"));
                func.setFuncLogin(rs.getString("func_login"));
                func.setFuncNivel(rs.getInt("func_nivel"));
            }
            conexao.fechar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return func;
    }


    @Override
    public List<Funcionario> get(String filtro) {

        String SQL = "SELECT * FROM funcionario WHERE func_nome = ?";

        SingletonDB.conectar();
        List<Funcionario> func = new ArrayList<>();

        try{


            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, filtro);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Funcionario funcionariosAdd = new Funcionario();
                funcionariosAdd.setFuncNome(rs.getString("func_nome"));
                funcionariosAdd.setFuncCpf(rs.getString("func_cpf"));
                funcionariosAdd.setFuncSenha(rs.getString("func_senha"));
                funcionariosAdd.setFuncEmail(rs.getString("func_email"));
                funcionariosAdd.setFuncLogin(rs.getString("func_login"));
                funcionariosAdd.setFuncNivel(rs.getInt("func_nivel"));
                func.add(funcionariosAdd);
            }

            conexao.fechar();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return func;
    }
}
