package projetoSalf.mvc.dao;

<<<<<<< HEAD
import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
=======
import org.springframework.stereotype.Repository;

import projetoSalf.mvc.model.Funcionario;
import projetoSalf.mvc.util.SingletonDB;

>>>>>>> Geral
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
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

=======

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

    public Funcionario buscarPorEmail(String email) {
        String sql = String.format("SELECT * FROM funcionario WHERE func_email = '%s'", email);
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
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
            System.out.println("Erro ao buscar por e-mail: " + e.getMessage());
>>>>>>> Geral
        }
        return null;
    }

    @Override
<<<<<<< HEAD
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
=======
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
>>>>>>> Geral
