package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CbProdDAO {

    public boolean inserir(CbProd cbProd, Conexao conexao) {
        String sql = """
            INSERT INTO cb_prod (cb_prod_id, produto_prod_cod, cesta_basica_cb_cod, prod_qtd)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conexao.getConnect().prepareStatement(sql)) {
            stmt.setInt(1, cbProd.getCbProdId());
            stmt.setInt(2, cbProd.getProdutoProdCod());
            stmt.setInt(3, cbProd.getCestaBasicaCbCod());
            stmt.setFloat(4, cbProd.getProdQtd()); // ✅ aqui é float!

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir produto na cesta: " + e.getMessage());
            return false;
        }
    }
}
