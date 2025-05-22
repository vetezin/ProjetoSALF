package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.ListaCompraProd;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ListaCompraProdDAO implements IDAO<ListaCompraProd>{
    @Override
    public ListaCompraProd gravar(ListaCompraProd lcp) {
        String sql = """
            INSERT INTO lc_prod (lc_prod_qtd, lc_cod, prod_cod)
            VALUES (#1, #2, #3);
        """;
        sql = sql.replace("#1", String.valueOf(lcp.getLc_prod_qtd()))
                .replace("#2", String.valueOf(lcp.getLc_cod()))
                .replace("#3", String.valueOf(lcp.getProd_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lcp;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public ListaCompraProd alterar(ListaCompraProd lcp) {
        String sql = """
            UPDATE lc_prod SET 
                lc_prod_qtd = #1
            WHERE lc_cod = #2 AND prod_cod = #3;
        """;
        sql = sql.replace("#1", String.valueOf(lcp.getLc_prod_qtd()))
                .replace("#2", String.valueOf(lcp.getLc_cod()))
                .replace("#3", String.valueOf(lcp.getProd_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lcp;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(ListaCompraProd lcp) {
        if (lcp == null) return false;
        String sql = "DELETE FROM lc_prod WHERE lc_cod = " + lcp.getLc_cod() + " AND prod_cod = " + lcp.getProd_cod();
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public ListaCompraProd get(int id) {
        String sql = "SELECT * FROM lc_prod WHERE lc_cod = " + id;
        var rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new ListaCompraProd(
                        rs.getInt("lc_prod_qtd"),
                        rs.getInt("lc_cod"),
                        rs.getInt("prod_cod")
                );
            }
        } catch (SQLException | SQLException e) {
            System.out.println("Erro ao buscar produto da lista: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ListaCompraProd> get(String filtro) {
        List<ListaCompraProd> lista = new ArrayList<>();
        String sql = "SELECT * FROM lc_prod";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                lista.add(new ListaCompraProd(
                        rs.getInt("lc_prod_qtd"),
                        rs.getInt("lc_cod"),
                        rs.getInt("prod_cod")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos da lista: " + e.getMessage());
        }
        return lista;
    }

    public ListaCompraProd getTodos() {

    }
}
