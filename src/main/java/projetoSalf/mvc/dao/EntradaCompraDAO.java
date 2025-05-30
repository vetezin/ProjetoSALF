package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.model.ProdutoCompra;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class EntradaCompraDAO {

    public boolean registrarEntrada(Compra compra) {
        Connection conn = SingletonDB.getConexao().getConnect();

        try {
            String sqlEstoque = "INSERT INTO estoque (es_qtdprod, produto_prod_cod, es_dtvalidade) VALUES (?, ?, ?)";
            PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque);

            for (ProdutoCompra pc : compra.getItens()) {
                stmtEstoque.setString(1, String.valueOf(pc.getQtd())); // varchar
                stmtEstoque.setInt(2, pc.getProdCod());
                stmtEstoque.setDate(3, Date.valueOf("2025-12-31")); // data genérica

                stmtEstoque.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            System.err.println("Erro ao registrar entrada: " + e.getMessage());
            return false;
        }
    }

    // ✅ NOVO: atualizar o estoque dos produtos
    public boolean incrementarEstoque(int prodCod, int qtd) {
        Connection conn = SingletonDB.getConexao().getConnect();

        try {
            String sql = "UPDATE estoque SET es_qtdprod = es_qtdprod + ? WHERE produto_prod_cod = ?";
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

}
