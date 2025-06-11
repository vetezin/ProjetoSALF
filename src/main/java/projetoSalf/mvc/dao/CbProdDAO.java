package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CbProdDAO {

    public boolean inserir(CbProd cbProd, Conexao conexao) {
        String sql = "INSERT INTO cb_prod (cb_prod_id, produto_prod_cod, cesta_basica_cb_cod, prod_qtd) " +
                "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conexao.getConnect().prepareStatement(sql);
            stmt.setInt(1, cbProd.getCb_prod_id());
            stmt.setInt(2, cbProd.getProduto_prod_cod());
            stmt.setInt(3, cbProd.getCesta_basica_cb_cod());
            stmt.setInt(4, cbProd.getProd_qtd());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir produto na cesta:");
            e.printStackTrace();
            return false;
        }
    }
}
