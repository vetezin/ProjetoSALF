package projetoSalf.mvc.dao;

import projetoSalf.mvc.util.Conexao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EstoqueCestaDAO {

    public boolean reduzirEstoque(int codProduto, int qtdRetirada, Conexao conexao) {
        String sql = "UPDATE estoque " +
                "SET es_qtdprod = CAST(es_qtdprod AS INTEGER) - ? " +
                "WHERE produto_prod_cod = ? AND CAST(es_qtdprod AS INTEGER) >= ?";

        try {
            PreparedStatement stmt = conexao.getConnect().prepareStatement(sql);
            stmt.setInt(1, qtdRetirada);
            stmt.setInt(2, codProduto);
            stmt.setInt(3, qtdRetirada);

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }
}
