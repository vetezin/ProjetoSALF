package projetoSalf.mvc.dao;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.model.Parametrizacao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Service
public class ParametrizacaoDAO implements IDAO<Parametrizacao> {

    @Override
    public Object gravar(Parametrizacao pa) {
        String SQL = "INSERT INTO parametrizacao (pa_nome_empresa, pa_cnpj, pa_endereco, pa_telefone, pa_email, pa_caminho_logotipo ) VALUES (?,?,?,?,?,?)";

        try {
            if (!ExisteEmpresas()) {
                Connection con = SingletonDB.getConexao().getConnect();
                PreparedStatement stmt = con.prepareStatement(SQL);

                stmt.setString(1, pa.getNomeEmpresa());
                stmt.setString(2, pa.getCnpj());
                stmt.setString(3, pa.getEndereco());
                stmt.setString(4, pa.getTelefone());
                stmt.setString(5, pa.getEmail());
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
    }

    @Override
    public Object alterar(Parametrizacao pa) {
        String SQL = "UPDATE parametrizacao SET pa_nome_empresa = ?, pa_cnpj = ?, pa_endereco = ?, pa_telefone = ? WHERE pa_email = ?";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, pa.getNomeEmpresa());
            stmt.setString(2, pa.getCnpj());
            stmt.setString(3, pa.getEndereco());
            stmt.setString(4, pa.getTelefone());
            stmt.setString(5, pa.getEmail());

            if (stmt.executeUpdate() > 0) {
                return pa;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean apagar(Parametrizacao entidade) {
        return false; // Não implementado ainda
    }

    @Override
    public Parametrizacao get(int id) {
        return null; // Não implementado ainda
    }

    @Override
    public List<Parametrizacao> get(String filtro) {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar parametrização por email", e);
        }

        return null;
    }

    public boolean ExisteEmpresas() {
        String SQL = "SELECT COUNT(*) FROM parametrizacao";

        try {
            Connection con = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
