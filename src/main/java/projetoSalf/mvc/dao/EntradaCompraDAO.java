package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.model.ProdutoCompra;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EntradaCompraDAO {


    public boolean incrementarEstoque(int prodCod, int qtd) {
        Connection conn = SingletonDB.getConexao().getConnect();

        try {
            String sql = "UPDATE estoque SET es_qtdprod = es_qtdprod + ? WHERE id_produto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, qtd);
            stmt.setInt(2, prodCod);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarCompraComoConfirmada(int compraId) {
        try {
            String sql = "UPDATE compra SET co_confirmada = TRUE WHERE co_cod = ?";
            Connection conn = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, compraId);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao marcar compra como confirmada: " + e.getMessage());
            return false;
        }
    }


    public boolean decrementarEstoque(int prodCod, int qtd) {
        try {
            String sql = "UPDATE estoque SET es_qtdprod = es_qtdprod - ? WHERE id_produto = ?";
            Connection conn = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, qtd);
            stmt.setInt(2, prodCod);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    public boolean desfazerEntradaCompra(int compraId, List<ProdutoCompra> produtos) {
        Connection conn = SingletonDB.getConexao().getConnect();

        String sqlBuscarQtd = """
        SELECT cpc_qtd FROM compra_prod_cot
        WHERE compra_cod = ? AND prod_cod = ? AND lc_cod = ? AND cot_cod = ? AND forn_cod = ?
    """;

        String sqlEstoque = "UPDATE estoque SET es_qtdprod = es_qtdprod - ? WHERE id_produto = ?";
        String sqlZerarCompra = "UPDATE compra_prod_cot SET cpc_qtd = 0 WHERE compra_cod = ? AND prod_cod = ? AND lc_cod = ? AND cot_cod = ? AND forn_cod = ?";

        try (PreparedStatement stmtBuscarQtd = conn.prepareStatement(sqlBuscarQtd);
             PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque);
             PreparedStatement stmtZerar = conn.prepareStatement(sqlZerarCompra)) {

            for (ProdutoCompra p : produtos) {
                // Buscar a quantidade real inserida
                stmtBuscarQtd.setInt(1, compraId);
                stmtBuscarQtd.setInt(2, p.getProdCod());
                stmtBuscarQtd.setInt(3, p.getLcCod());
                stmtBuscarQtd.setInt(4, p.getCotCod());
                stmtBuscarQtd.setInt(5, p.getFornCod());

                ResultSet rs = stmtBuscarQtd.executeQuery();
                int qtdAtual = 0;
                if (rs.next()) {
                    qtdAtual = rs.getInt("cpc_qtd");
                }

                if (qtdAtual > 0) {
                    // Subtrair do estoque
                    stmtEstoque.setInt(1, qtdAtual);
                    stmtEstoque.setInt(2, p.getProdCod());
                    stmtEstoque.addBatch();

                    // Zerar o item da compra
                    stmtZerar.setInt(1, compraId);
                    stmtZerar.setInt(2, p.getProdCod());
                    stmtZerar.setInt(3, p.getLcCod());
                    stmtZerar.setInt(4, p.getCotCod());
                    stmtZerar.setInt(5, p.getFornCod());
                    stmtZerar.addBatch();
                }
            }

            stmtEstoque.executeBatch();
            stmtZerar.executeBatch();

            // Desmarcar confirmação da compra
            String sqlDesconfirmar = "UPDATE compra SET co_confirmada = false WHERE co_cod = ?";
            try (PreparedStatement stmtConf = conn.prepareStatement(sqlDesconfirmar)) {
                stmtConf.setInt(1, compraId);
                stmtConf.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao desfazer entrada: " + e.getMessage());
            return false;
        }
    }


    public boolean atualizarItensCompra(int compraId, List<ProdutoCompra> itens) {
        try {
            Connection conn = SingletonDB.getConexao().getConnect();

            for (ProdutoCompra p : itens) {
                String sql = "UPDATE compra_prod_cot SET cpc_qtd = ?, cpc_valorcompra = ? " +
                        "WHERE compra_cod = ? AND prod_cod = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, p.getQtd());
                stmt.setDouble(2, p.getValorCompra());
                stmt.setInt(3, compraId);
                stmt.setInt(4, p.getProdCod());
                stmt.executeUpdate();

                atualizarQuantidadeListaCompra(p.getLcCod(), p.getProdCod(), p.getQtd());
            }

            return true;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar itens da compra: " + e.getMessage());
            return false;
        }
    }


    public boolean atualizarQuantidadeListaCompra(int lcCod, int prodCod, int qtdComprada) {
        String sql = "UPDATE lc_prod SET lc_prod_qtd = lc_prod_qtd - ? WHERE lc_cod = ? AND prod_cod = ?";
        try {
            Connection conn = SingletonDB.getConexao().getConnect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, qtdComprada);
            stmt.setInt(2, lcCod);
            stmt.setInt(3, prodCod);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar quantidade da lista de compra: " + e.getMessage());
            return false;
        }
    }




}
