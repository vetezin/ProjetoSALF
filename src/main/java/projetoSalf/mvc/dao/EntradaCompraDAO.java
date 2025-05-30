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


}
