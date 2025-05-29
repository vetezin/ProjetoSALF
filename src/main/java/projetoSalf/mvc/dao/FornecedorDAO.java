package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.FormatUtils; // Mantenha esta importação
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FornecedorDAO implements IDAO<Fornecedor> {

    @Override
    public Fornecedor gravar(Fornecedor fornecedor) {
        String cnpjLimpo = FormatUtils.limparCNPJ(fornecedor.getForn_cnpj());
        fornecedor.setForn_cnpj(cnpjLimpo);

        // Checagem de duplicidade de CNPJ
        String filtroCnpj = "forn_cnpj = '" + cnpjLimpo.replace("'", "''") + "'";
        List<Fornecedor> jaExiste = get(filtroCnpj);

        if (!jaExiste.isEmpty()) {
            System.out.println("Erro: Fornecedor com CNPJ " + cnpjLimpo + " já existe.");
            return null; // Retorna null se já existe
        }

        // SQL para inserção com os campos corretos e RETURNING para obter o ID
        String sql = String.format(
                "INSERT INTO fornecedor(forn_nome, forn_end, forn_cnpj, forn_telefone) " +
                        "VALUES ('%s', '%s', '%s', '%s') RETURNING forn_cod;",
                fornecedor.getForn_nome().replace("'", "''"),
                fornecedor.getForn_end() != null ? fornecedor.getForn_end().replace("'", "''") : "",
                fornecedor.getForn_cnpj().replace("'", "''"),
                fornecedor.getForn_telefone() != null ? fornecedor.getForn_telefone().replace("'", "''") : ""
        );

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            if (rs != null && rs.next()) {
                fornecedor.setForn_cod(rs.getInt("forn_cod")); // Preenche o ID
                return fornecedor;
            } else {
                System.out.println("Erro: Não foi possível obter o ID do fornecedor após gravação.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar fornecedor: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Fornecedor alterar(Fornecedor fornecedor) {
        String cnpjLimpo = FormatUtils.limparCNPJ(fornecedor.getForn_cnpj());
        fornecedor.setForn_cnpj(cnpjLimpo);

        // Checagem de duplicidade de CNPJ para alteração
        String filtroCnpj = "forn_cnpj = '" + cnpjLimpo.replace("'", "''") + "' AND forn_cod <> " + fornecedor.getForn_cod();
        List<Fornecedor> jaExiste = get(filtroCnpj);

        if (!jaExiste.isEmpty()) {
            System.out.println("Erro: Fornecedor com CNPJ " + cnpjLimpo + " já existe para outro ID.");
            return null; // Retorna null se já existe outro fornecedor com o mesmo CNPJ
        }

        // SQL para alteração com os campos corretos
        String sql = String.format(
                "UPDATE fornecedor SET " +
                        "forn_nome = '%s', " +
                        "forn_end = '%s', " +
                        "forn_cnpj = '%s', " +
                        "forn_telefone = '%s' " +
                        "WHERE forn_cod = %d;",
                fornecedor.getForn_nome().replace("'", "''"),
                fornecedor.getForn_end() != null ? fornecedor.getForn_end().replace("'", "''") : "",
                fornecedor.getForn_cnpj().replace("'", "''"),
                fornecedor.getForn_telefone() != null ? fornecedor.getForn_telefone().replace("'", "''") : "",
                fornecedor.getForn_cod()
        );

        if (SingletonDB.getConexao().manipular(sql)) {
            return fornecedor;
        } else {
            System.out.println("Erro ao alterar fornecedor: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Fornecedor get(int id) {
        String sql = "SELECT * FROM fornecedor WHERE forn_cod = " + id;
        Fornecedor fornecedor = null;

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            if (rs != null && rs.next()) {
                fornecedor = new Fornecedor();
                fornecedor.setForn_cod(rs.getInt("forn_cod"));
                fornecedor.setForn_nome(rs.getString("forn_nome"));
                fornecedor.setForn_end(rs.getString("forn_end")); // Alterado para forn_end
                fornecedor.setForn_cnpj(rs.getString("forn_cnpj"));
                fornecedor.setForn_telefone(rs.getString("forn_telefone"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar fornecedor com ID " + id + ": " + e.getMessage());
        }
        return fornecedor;
    }

    @Override
    public List<Fornecedor> get(String filtro) {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";
        if (filtro != null && !filtro.isEmpty()) {
            // Ajustado para pesquisar nos campos existentes
            sql += String.format(" WHERE forn_nome ILIKE '%%%s%%' OR forn_end ILIKE '%%%s%%' OR forn_cnpj ILIKE '%%%s%%'",
                    filtro, filtro, filtro);
        }

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs != null && rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setForn_cod(rs.getInt("forn_cod"));
                fornecedor.setForn_nome(rs.getString("forn_nome"));
                fornecedor.setForn_end(rs.getString("forn_end")); // Alterado para forn_end
                fornecedor.setForn_cnpj(rs.getString("forn_cnpj"));
                fornecedor.setForn_telefone(rs.getString("forn_telefone"));
                fornecedores.add(fornecedor);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar fornecedores: " + e.getMessage());
        }
        return fornecedores;
    }

    @Override
    public boolean apagar(Fornecedor fornecedor) {
        if (fornecedor == null || fornecedor.getForn_cod() <= 0) {
            return false;
        }
        String sql = "DELETE FROM fornecedor WHERE forn_cod = " + fornecedor.getForn_cod();
        return SingletonDB.getConexao().manipular(sql);
    }
}