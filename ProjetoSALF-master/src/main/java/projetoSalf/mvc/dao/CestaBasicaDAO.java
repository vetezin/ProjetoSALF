package projetoSalf.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import projetoSalf.mvc.model.CestaBasica;

public class CestaBasicaDAO {
    public int inserir(CestaBasica cesta, Connection conn) throws Exception {
        String sql = "INSERT INTO cesta_basica (cb_motivo, cb_dtcriacao, pessoa_carente_pc_cod, func_cod, cb_dtdoacao) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING cb_cod";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cesta.getCbMotivo());
        stmt.setDate(2, java.sql.Date.valueOf(cesta.getCbDtCriacao()));
        stmt.setInt(3, cesta.getPessoaCarentePcCod());
        stmt.setInt(4, cesta.getFuncCod());
        stmt.setDate(5, java.sql.Date.valueOf(cesta.getCbDtDoacao()));

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("cb_cod");
        } else {
            throw new Exception("Erro ao inserir cesta básica: ID não retornado.");
        }
    }
}
