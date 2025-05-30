package projetoSalf.mvc.dao;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Parametrizacao;
<<<<<<< HEAD
import projetoSalf.mvc.util.Conexao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
=======
import projetoSalf.mvc.util.SingletonDB;

>>>>>>> Geral
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
<<<<<<< HEAD
@Service
public class ParametrizacaoDAO implements IDAO<Parametrizacao>{


    @Override
    public Object gravar(Parametrizacao pa) {

        String SQL = "INSERT INTO parametrizacao (pa_nome_empresa, pa_cnpj, pa_endereco, pa_telefone, pa_email, pa_caminho_logotipo ) VALUES (?,?,?,?,?,?)";

        try{
            if(ExisteEmpresas() == false) {
                Conexao conexao = new Conexao();
                Connection con = conexao.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL);



                // Salvar dados no banco
=======

@Service
public class ParametrizacaoDAO implements IDAO<Parametrizacao> {

    @Override
    public Object gravar(Parametrizacao pa) {
        String SQL = "INSERT INTO parametrizacao (pa_nome_empresa, pa_cnpj, pa_endereco, pa_telefone, pa_email, pa_caminho_logotipo ) VALUES (?,?,?,?,?,?)";

        try {
            if (!ExisteEmpresas()) {
                Connection con = SingletonDB.getConexao().getConnect();
                PreparedStatement stmt = con.prepareStatement(SQL);

>>>>>>> Geral
                stmt.setString(1, pa.getNomeEmpresa());
                stmt.setString(2, pa.getCnpj());
                stmt.setString(3, pa.getEndereco());
                stmt.setString(4, pa.getTelefone());
                stmt.setString(5, pa.getEmail());
<<<<<<< HEAD
                stmt.setBytes(6,pa.getLogotipo());
                if (stmt.executeUpdate() > 0) {
                    conexao.fechar();
                    return pa;

                }
            }
            else{
                System.out.println("Empresa já existente, não é possivel cadastrar outra empresa nesse sistema");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir",e);
        }

        return null;

=======
                stmt.setBytes(6, pa.getLogotipo());

                if (stmt.executeUpdate() > 0) {
                    return pa;
                }
            } else {
                System.out.println("Empresa já existente, não é possível cadastrar outra empresa nesse sistema.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inserir", e);
        }

        return null;
>>>>>>> Geral
    }

    @Override
    public Object alterar(Parametrizacao pa) {
<<<<<<< HEAD

        String SQL = "UPDATE parametrizacao SET pa_nome_empresa = ?, pa_cnpj = ?, pa_endereco = ?, pa_telefone = ? WHERE pa_email = ?";
        try {
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
=======
        String SQL = "UPDATE parametrizacao SET pa_nome_empresa = ?, pa_cnpj = ?, pa_endereco = ?, pa_telefone = ?, pa_caminho_logotipo = ? WHERE pa_email = ?";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
>>>>>>> Geral
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pa.getNomeEmpresa());
            stmt.setString(2, pa.getCnpj());
            stmt.setString(3, pa.getEndereco());
            stmt.setString(4, pa.getTelefone());
<<<<<<< HEAD
            stmt.setString(5, pa.getEmail());


            if(stmt.executeUpdate() > 0){
                conexao.fechar();
                return pa;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
=======
            stmt.setBytes(5, pa.getLogotipo());  // <<< adicionando logotipo
            stmt.setString(6, pa.getEmail());

            if (stmt.executeUpdate() > 0) {
                return pa;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

>>>>>>> Geral
        return null;
    }

    @Override
    public boolean apagar(Parametrizacao entidade) {
<<<<<<< HEAD
        return false;
=======
        return false; // Não implementado ainda
>>>>>>> Geral
    }

    @Override
    public Parametrizacao get(int id) {
<<<<<<< HEAD
        return null;
=======
        return null; // Não implementado ainda
>>>>>>> Geral
    }

    @Override
    public List<Parametrizacao> get(String filtro) {
<<<<<<< HEAD
        return List.of();
    }


    public boolean existeRegistro(Parametrizacao PA) {

        String SQL = "SELECT pa_email FROM parametrizacao WHERE pa_email = ?";

        try{
            Conexao conexao = new Conexao();

            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1,PA.getEmail());

            ResultSet rs = stmt.executeQuery();


            //Se nao encontrou nenhum registro, retorna false, alegando isso.
            if(!rs.next()) {
                conexao.fechar();
                return false;
            }
            return true;
=======
        return List.of(); // Não implementado ainda
    }

    public boolean existeRegistro(Parametrizacao pa) {
        String SQL = "SELECT pa_email FROM parametrizacao WHERE pa_email = ?";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pa.getEmail());

            ResultSet rs = stmt.executeQuery();

            return rs.next();
>>>>>>> Geral

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
<<<<<<< HEAD

    }


    public Parametrizacao getRegistro(String email) {

        String SQL = "SELECT * FROM parametrizacao WHERE pa_email = ?";

        try {

            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1,email);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){

                Parametrizacao PA = new Parametrizacao();
                PA.setId(rs.getInt("pa_id"));
                PA.setNomeEmpresa(rs.getString("pa_nome_empresa"));
                PA.setCnpj(rs.getString("pa_cnpj"));
                PA.setEndereco(rs.getString("pa_endereco"));
                PA.setTelefone(rs.getString("pa_telefone"));
                PA.setEmail(rs.getString("pa_email"));
                byte[] logotipoBytes = rs.getBytes("pa_caminho_logotipo"); // nome da coluna exato do seu banco
                PA.setLogotipo(logotipoBytes);

                return PA;


            }
            conexao.fechar();

=======
    }

    public Parametrizacao getRegistro(String email) {
        String SQL = "SELECT * FROM parametrizacao WHERE pa_email = ?";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Parametrizacao pa = new Parametrizacao();
                pa.setId(rs.getInt("pa_id"));
                pa.setNomeEmpresa(rs.getString("pa_nome_empresa"));
                pa.setCnpj(rs.getString("pa_cnpj"));
                pa.setEndereco(rs.getString("pa_endereco"));
                pa.setTelefone(rs.getString("pa_telefone"));
                pa.setEmail(rs.getString("pa_email"));
                pa.setLogotipo(rs.getBytes("pa_caminho_logotipo"));

                return pa;
            }
>>>>>>> Geral

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar parametrização por email", e);
        }
<<<<<<< HEAD
        return null;
    }


=======

        return null;
    }

>>>>>>> Geral
    public boolean ExisteEmpresas() {
        String SQL = "SELECT COUNT(*) FROM parametrizacao";

        try {
<<<<<<< HEAD
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
=======
            Connection con = SingletonDB.getConexao().getConnect();
>>>>>>> Geral
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
<<<<<<< HEAD
                return count > 0; // só retorna true se houver 1 ou mais registros
            }

            return false; // caso não consiga ler o resultado
=======
                return count > 0;
            }

            return false;
>>>>>>> Geral
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
<<<<<<< HEAD

=======
>>>>>>> Geral
}
