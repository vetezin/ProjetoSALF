package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.AcertoEstoque;
import projetoSalf.mvc.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AcertoEstoqueDAO implements IDAO<AcertoEstoque> {

    @Override
    public AcertoEstoque gravar(AcertoEstoque ae) {
        String sql = """
            INSERT INTO acerto_estoque(ae_data, ae_motivo, ae_qtd, prod_cod, func_cod)
            VALUES ('#1', '#2', #3, #4, #5) RETURNING ae_cod;
        """;
        sql = sql.replace("#1", ae.getData());
        sql = sql.replace("#2", ae.getMotivo().replace("'", "''"));
        sql = sql.replace("#3", String.valueOf(ae.getQtd()));
        sql = sql.replace("#4", String.valueOf(ae.getCodProduto()));
        sql = sql.replace("#5", String.valueOf(ae.getCodFuncionario()));

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs != null && rs.next()) {
                ae.setCod(rs.getInt("ae_cod"));
                return ae;
            } else {
                System.out.println("Erro ao recuperar ID do acerto de estoque.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar acerto de estoque: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AcertoEstoque alterar(AcertoEstoque ae) {
        String sql = """
            UPDATE acerto_estoque SET
                ae_data = '#1',
                ae_motivo = '#2',
                ae_qtd = #3,
                prod_cod = #4,
                func_cod = #5
            WHERE ae_cod = #6;
        """;
        sql = sql.replace("#1", ae.getData());
        sql = sql.replace("#2", ae.getMotivo().replace("'", "''"));
        sql = sql.replace("#3", String.valueOf(ae.getQtd()));
        sql = sql.replace("#4", String.valueOf(ae.getCodProduto()));
        sql = sql.replace("#5", String.valueOf(ae.getCodFuncionario()));
        sql = sql.replace("#6", String.valueOf(ae.getCod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return ae;
        } else {
            System.out.println("Erro ao alterar acerto de estoque: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public AcertoEstoque get(int id) {
        String sql = "SELECT * FROM acerto_estoque WHERE ae_cod = " + id;
        AcertoEstoque ae = null;
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                ae = new AcertoEstoque(
                        rs.getInt("ae_cod"),
                        rs.getString("ae_data"),
                        rs.getString("ae_motivo"),
                        rs.getInt("ae_qtd"),
                        rs.getInt("prod_cod"),
                        rs.getInt("func_cod")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar acerto de estoque: " + e.getMessage());
        }
        return ae;
    }

    @Override
    public List<AcertoEstoque> get(String filtro) {
        List<AcertoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM acerto_estoque";
        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                lista.add(new AcertoEstoque(
                        rs.getInt("ae_cod"),
                        rs.getString("ae_data"),
                        rs.getString("ae_motivo"),
                        rs.getInt("ae_qtd"),
                        rs.getInt("prod_cod"),
                        rs.getInt("func_cod")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar acertos de estoque: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean apagar(AcertoEstoque ae) {
        if (ae == null) return false;
        String sql = "DELETE FROM acerto_estoque WHERE ae_cod = " + ae.getCod();
        return SingletonDB.getConexao().manipular(sql);
    }
}
