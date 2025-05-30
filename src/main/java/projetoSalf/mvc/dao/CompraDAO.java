package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {
    public int inserir(Compra compra, Conexao conexao) {
        String sql = "INSERT INTO compra (co_descricao, co_valortotal, co_dtcompra, co_totalitens, funcionario_func_cod, cot_forn_cotacao_cot_cod, cot_forn_fornecedor_forn_cod) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING co_cod";

        try (Connection conn = conexao.getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, compra.getCoDescricao());
            stmt.setFloat(2, compra.getCoValorTotal());
            stmt.setObject(3, compra.getCoDtCompra());
            stmt.setInt(4, compra.getCoTotalItens());
            stmt.setInt(5, compra.getFuncionarioFuncCod());
            stmt.setInt(6, compra.getCotFornCotacaoCotCod());
            stmt.setInt(7, compra.getCotFornFornecedorFornCod());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("co_cod");

        } catch (Exception e) {
            System.err.println("Erro ao inserir Compra: " + e.getMessage());
        }
        return -1;
    }

    public List<Compra> consultarNaoConfirmadas(Conexao conexao) {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT * FROM compra WHERE co_confirmada = FALSE ORDER BY co_cod DESC";

        try (Connection conn = conexao.getConnect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Compra c = new Compra();
                c.setCoCod(rs.getInt("co_cod"));
                c.setCoDescricao(rs.getString("co_descricao"));
                c.setCoValorTotal(rs.getFloat("co_valortotal"));
                c.setCoDtCompra(rs.getDate("co_dtcompra").toLocalDate());
                c.setCoTotalItens(rs.getInt("co_totalitens"));
                c.setFuncionarioFuncCod(rs.getInt("funcionario_func_cod"));
                c.setCotFornCotacaoCotCod(rs.getInt("cot_forn_cotacao_cot_cod"));
                c.setCotFornFornecedorFornCod(rs.getInt("cot_forn_fornecedor_forn_cod"));
                c.setCoConfirmada(rs.getBoolean("co_confirmada"));
                lista.add(c);
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar compras: " + e.getMessage());
        }

        return lista;
    }

    public boolean marcarComoConfirmada(int compraId) {
        String sql = "UPDATE compra SET co_confirmada = TRUE WHERE co_cod = ?";

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, compraId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao confirmar compra: " + e.getMessage());
            return false;
        }
    }
}