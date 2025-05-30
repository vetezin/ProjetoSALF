package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public int inserir(Compra compra, Conexao conexao) {
        String sql = "INSERT INTO compra (co_descricao, co_valortotal, co_dtcompra, co_totalitens, funcionario_func_cod, cot_forn_cotacao_cot_cod, cot_forn_fornecedor_forn_cod) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING co_cod";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conexao.getConnect();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, compra.getCoDescricao());
            stmt.setFloat(2, compra.getCoValorTotal());
            stmt.setObject(3, compra.getCoDtCompra());
            stmt.setInt(4, compra.getCoTotalItens());
            stmt.setInt(5, compra.getFuncionarioFuncCod());
            stmt.setInt(6, compra.getCotFornCotacaoCotCod());
            stmt.setInt(7, compra.getCotFornFornecedorFornCod());

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("co_cod");
            }

        } catch (Exception e) {
            System.err.println("Erro ao inserir Compra: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            // NÃO fecha conn aqui!
        }

        return -1;
    }



    public List<Compra> consultarTodas(Conexao conexao) {
        List<Compra> lista = new ArrayList<>();

        String sql = "SELECT * FROM compra ORDER BY co_cod DESC";

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
                c.setFuncionarioFuncCod(rs.getInt("funcionario_funccod"));
                c.setCotFornCotacaoCotCod(rs.getInt("cot_forn_cotacao_cotcod"));
                c.setCotFornFornecedorFornCod(rs.getInt("cot_forn_fornecedor_forncod"));

                lista.add(c);
            }

        } catch (Exception e) {
            System.err.println("Erro ao consultar compras: " + e.getMessage());
        }

        return lista;
    }
}