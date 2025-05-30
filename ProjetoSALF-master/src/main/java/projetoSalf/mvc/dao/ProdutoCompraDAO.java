package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.ProdutoCompra;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

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


}
