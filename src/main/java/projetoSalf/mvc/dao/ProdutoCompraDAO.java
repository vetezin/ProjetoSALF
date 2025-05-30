package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.ProdutoCompra;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoCompraDAO {

    public boolean inserir(ProdutoCompra produto, int codCompra, Conexao conexao) {
        String sql = "INSERT INTO compra_prod_cot (lc_cod, prod_cod, cot_cod, forn_cod, compra_cod, cpc_qtd, cpc_valorcompra) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = conexao.getConnect(); // usa a conexão passada
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, produto.getLcCod());
            stmt.setInt(2, produto.getProdCod());
            stmt.setInt(3, produto.getCotCod());
            stmt.setInt(4, produto.getFornCod());
            stmt.setInt(5, codCompra);
            stmt.setInt(6, produto.getQtd());
            stmt.setFloat(7, produto.getValorCompra());

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("Erro ao inserir ProdutoCompra: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            // NÃO fecha conn aqui!
        }
    }

    public List<Map<String, Object>> getProdutosDaCompra(int compraId, Conexao conexao) {
        List<Map<String, Object>> itens = new ArrayList<>();
        String sql = """
        SELECT 
            cpc.prod_cod,
            p.prod_desc,
            cpc.cpc_qtd,
            cpc.cpc_valorcompra,
            cpc.lc_cod,
            cpc.cot_cod,
            cpc.forn_cod
        FROM compra_prod_cot cpc
        JOIN produto p ON p.prod_cod = cpc.prod_cod
        WHERE cpc.compra_cod = ?
        """;

        try (PreparedStatement stmt = conexao.getConnect().prepareStatement(sql)) {
            stmt.setInt(1, compraId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("prod_cod", rs.getInt("prod_cod"));
                item.put("prod_desc", rs.getString("prod_desc"));
                item.put("cpc_qtd", rs.getInt("cpc_qtd"));
                item.put("cpc_valorcompra", rs.getFloat("cpc_valorcompra"));
                item.put("lc_cod", rs.getInt("lc_cod"));
                item.put("cot_cod", rs.getInt("cot_cod"));
                item.put("forn_cod", rs.getInt("forn_cod"));
                itens.add(item);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar itens da compra: " + e.getMessage());
        }

        return itens;
    }
}
