package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.util.Conexao;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CestaBasicaDAO {
    public boolean inserir(CestaBasica cesta, Conexao conexao) {
        String sql = "INSERT INTO cesta_basica " +
                "(cb_motivo, cb_dtcriacao, pessoa_carente_pc_cod, cb_codfunc, funcionario_func_cod, cb_dtdoacao) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conexao.getConnect().prepareStatement(sql);
            stmt.setString(1, cesta.getCbMotivo());
            stmt.setDate(2, java.sql.Date.valueOf(cesta.getCbDtCriacao()));
            stmt.setInt(3, cesta.getPessoaCarentePcCod());
            stmt.setInt(4, cesta.getCbCodFunc());
            stmt.setInt(5, cesta.getFuncionarioFuncCod());
            stmt.setDate(6, java.sql.Date.valueOf(cesta.getCbDtDoacao()));

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir cesta básica: " + e.getMessage());
            return false;
        }
    }
}
