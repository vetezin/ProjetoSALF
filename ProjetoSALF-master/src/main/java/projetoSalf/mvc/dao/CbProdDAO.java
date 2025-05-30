package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.CbProd;
<<<<<<< HEAD
import java.sql.Connection;
=======
import projetoSalf.mvc.util.Conexao;

>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CbProdDAO {

<<<<<<< HEAD
    public boolean inserir(CbProd cbProd, Connection conn) {
=======
    public boolean inserir(CbProd cbProd, Conexao conexao) {
>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
        String sql = """
            INSERT INTO cb_prod (cb_prod_id, produto_prod_cod, cesta_basica_cb_cod, prod_qtd)
            VALUES (?, ?, ?, ?)
        """;

<<<<<<< HEAD
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cbProd.getCbProdId());
            stmt.setInt(2, cbProd.getProdutoProdCod());
            stmt.setInt(3, cbProd.getCestaBasicaCbCod());
            stmt.setFloat(4, cbProd.getProdQtd());
=======
        try (PreparedStatement stmt = conexao.getConnect().prepareStatement(sql)) {
            stmt.setInt(1, cbProd.getCbProdId());
            stmt.setInt(2, cbProd.getProdutoProdCod());
            stmt.setInt(3, cbProd.getCestaBasicaCbCod());
            stmt.setFloat(4, cbProd.getProdQtd()); // ✅ aqui é float!
>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao inserir produto na cesta: " + e.getMessage());
            return false;
        }
    }
}
