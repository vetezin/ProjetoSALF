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
        cpc.forn_cod,
        lp.lc_prod_qtd
    FROM compra_prod_cot cpc
    JOIN produto p ON p.prod_cod = cpc.prod_cod
    JOIN lc_prod lp ON lp.lc_cod = cpc.lc_cod AND lp.prod_cod = cpc.prod_cod
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
                item.put("lc_prod_qtd", rs.getInt("lc_prod_qtd")); // quantidade da lista original
                item.put("faltando", rs.getInt("lc_prod_qtd") - rs.getInt("cpc_qtd")); // calculado
                itens.add(item);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar itens da compra: " + e.getMessage());
        }

        return itens;
    }

    public int buscarQtdAtual(int compraCod, int prodCod, int lcCod, int cotCod, int fornCod, Conexao conexao) {
        Connection conn = conexao.getConnect(); // ✅ usa corretamente
        if (conn == null) return 0;

        String sql = """
        SELECT cpc_qtd FROM compra_prod_cot
        WHERE compra_cod = ? AND prod_cod = ? AND lc_cod = ? AND cot_cod = ? AND forn_cod = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, compraCod);
            stmt.setInt(2, prodCod);
            stmt.setInt(3, lcCod);
            stmt.setInt(4, cotCod);
            stmt.setInt(5, fornCod);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cpc_qtd");
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar quantidade atual: " + e.getMessage());
        }

        return 0;
    }


    public boolean atualizarItemCompra(int compraId, int prodCod, int lcCod, int cotCod, int fornCod, int novaQtd, float novoValor) {
        System.out.println("🔧 Atualizando item:");
        System.out.println("compraId=" + compraId + ", prodCod=" + prodCod + ", lcCod=" + lcCod + ", cotCod=" + cotCod + ", fornCod=" + fornCod);

        String sql = "UPDATE compra_prod_cot SET cpc_qtd = ?, cpc_valorcompra = ? " +
                "WHERE compra_cod = ? AND prod_cod = ? AND lc_cod = ? " +
                "AND cot_cod = ? AND forn_cod = ?";

        try {
            int qtdAtual = buscarQtdAtual(compraId, prodCod, lcCod, cotCod, fornCod, SingletonDB.getConexao());
            int novaQtdTotal = qtdAtual + novaQtd;

            PreparedStatement stmt = SingletonDB.getConexao().getConnect().prepareStatement(sql);
            stmt.setInt(1, novaQtdTotal);
            stmt.setFloat(2, novoValor);
            stmt.setInt(3, compraId);
            stmt.setInt(4, prodCod);
            stmt.setInt(5, lcCod);
            stmt.setInt(6, cotCod);
            stmt.setInt(7, fornCod);

            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("↪ Linhas afetadas: " + linhasAfetadas);
            return linhasAfetadas > 0;

        } catch (Exception e) {
            System.err.println("❌ Erro ao atualizar item da compra: " + e.getMessage());
            return false;
        }
    }

    public boolean excluirItemDaCompra(int lcCod, int prodCod, int cotCod, int fornCod, int compraCod) {
        String sql = """
        DELETE FROM compra_prod_cot
        WHERE lc_cod = ? AND prod_cod = ? AND cot_cod = ? AND forn_cod = ? AND compra_cod = ?
    """;

        try (Connection conn = SingletonDB.getConexao().getConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lcCod);
            stmt.setInt(2, prodCod);
            stmt.setInt(3, cotCod);
            stmt.setInt(4, fornCod);
            stmt.setInt(5, compraCod);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (Exception e) {
            System.err.println("Erro ao excluir item da compra: " + e.getMessage());
            return false;
        }
    }











}
