package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.ListaCompra;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ListaCompraDAO implements IDAO<ListaCompra> {

    @Override
    public ListaCompra gravar(ListaCompra lc) {
        String sql = """
            INSERT INTO listacompra (lc_dtlista, lc_desc, func_cod)
            VALUES ('#1', '#2', #3);
        """;
        sql = sql.replace("#1", lc.getLc_dtlLista())
                .replace("#2", lc.getLc_desc())
                .replace("#3", String.valueOf(lc.getFunc_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lc;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public ListaCompra alterar(ListaCompra lc) {
        String sql = """
            UPDATE listacompra SET 
                lc_dtlista = '#1',
                lc_desc = '#2',
                func_cod = #3
            WHERE lc_cod = #4;
        """;
        sql = sql.replace("#1", lc.getLc_dtlLista())
                .replace("#2", lc.getLc_desc())
                .replace("#3", String.valueOf(lc.getFunc_cod()))
                .replace("#4", String.valueOf(lc.getLc_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lc;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(ListaCompra lc) {
        if (lc == null) return false;
        String sql = "DELETE FROM listacompra WHERE lc_cod = " + lc.getLc_cod();
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public ListaCompra get(int id) {
        String sql = "SELECT * FROM listacompra WHERE lc_cod = " + id;
        var rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new ListaCompra(
                        rs.getInt("lc_cod"),
                        rs.getString("lc_dtlista"),
                        rs.getString("lc_desc"),
                        rs.getInt("func_cod")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista de compra: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ListaCompra> get(String filtro) {
        List<ListaCompra> lista = new ArrayList<>();
        String sql = "SELECT * FROM listacompra";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                lista.add(new ListaCompra(
                        rs.getInt("lc_cod"),
                        rs.getString("lc_dtlista"),
                        rs.getString("lc_desc"),
                        rs.getInt("func_cod")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar listas de compra: " + e.getMessage());
        }
        return lista;
    }
}
