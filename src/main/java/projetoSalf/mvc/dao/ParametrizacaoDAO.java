package projetoSalf.mvc.dao;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Parametrizacao;
import projetoSalf.mvc.util.Conexao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
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
                stmt.setString(1, pa.getNomeEmpresa());
                stmt.setString(2, pa.getCnpj());
                stmt.setString(3, pa.getEndereco());
                stmt.setString(4, pa.getTelefone());
                stmt.setString(5, pa.getEmail());
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

    }

    @Override
    public Object alterar(Parametrizacao pa) {

        String SQL = "UPDATE parametrizacao SET pa_nome_empresa = ?, pa_cnpj = ?, pa_endereco = ?, pa_telefone = ? WHERE pa_email = ?";
        try {
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pa.getNomeEmpresa());
            stmt.setString(2, pa.getCnpj());
            stmt.setString(3, pa.getEndereco());
            stmt.setString(4, pa.getTelefone());
            stmt.setString(5, pa.getEmail());


            if(stmt.executeUpdate() > 0){
                conexao.fechar();
                return pa;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean apagar(Parametrizacao entidade) {
        return false;
    }

    @Override
    public Parametrizacao get(int id) {
        return null;
    }

    @Override
    public List<Parametrizacao> get(String filtro) {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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


        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar parametrização por email", e);
        }
        return null;
    }


    public boolean ExisteEmpresas() {
        String SQL = "SELECT COUNT(*) FROM parametrizacao";

        try {
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // só retorna true se houver 1 ou mais registros
            }

            return false; // caso não consiga ler o resultado
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
