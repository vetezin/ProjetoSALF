package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.ListaCompraProduto;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ListaCompraProdutoDAO {

    public ListaCompraProduto gravar(ListaCompraProduto lcp) {
        String sql = """
            INSERT INTO lc_prod (prod_cod, lc_cod, lc_prod_qtd)
            VALUES (#1, #2, #3);
        """;
        // As colunas no seu banco são 'prod_cod', 'lc_cod', 'lc_prod_qtd' conforme DDL.
        // O nome da tabela é 'lc_prod'. Ajustado aqui.

        sql = sql.replace("#1", String.valueOf(lcp.getProdutoCod()));
        sql = sql.replace("#2", String.valueOf(lcp.getListaCompraCod()));
        sql = sql.replace("#3", String.valueOf(lcp.getQtd()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lcp;
        } else {
            System.out.println("Erro ao inserir ListaCompraProduto: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    public ListaCompraProduto alterar(ListaCompraProduto lcp) {
        String sql = """
            UPDATE lc_prod
            SET lc_prod_qtd = #3
            WHERE prod_cod = #1 AND lc_cod = #2;
        """;
        // As colunas no seu banco são 'prod_cod', 'lc_cod', 'lc_prod_qtd' conforme DDL.
        // O nome da tabela é 'lc_prod'. Ajustado aqui.

        sql = sql.replace("#1", String.valueOf(lcp.getProdutoCod()));
        sql = sql.replace("#2", String.valueOf(lcp.getListaCompraCod()));
        sql = sql.replace("#3", String.valueOf(lcp.getQtd()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lcp;
        } else {
            System.out.println("Erro ao atualizar ListaCompraProduto: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    public boolean apagar(ListaCompraProduto lcp) {
        // Replicando o uso de String.format do SaidaProdDAO para DELETE
        String sql = String.format(
                "DELETE FROM lc_prod WHERE prod_cod = %d AND lc_cod = %d",
                lcp.getProdutoCod(), lcp.getListaCompraCod()
        );
        // O nome da tabela é 'lc_prod'. Ajustado aqui.

        return SingletonDB.getConexao().manipular(sql);
    }

    public ListaCompraProduto get(int produtoCod, int listaCompraCod) {
        // Replicando o uso de String.format do SaidaProdDAO para GET
        String sql = String.format(
                "SELECT * FROM lc_prod WHERE prod_cod = %d AND lc_cod = %d",
                produtoCod, listaCompraCod
        );
        // O nome da tabela é 'lc_prod'. Ajustado aqui.

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                return new ListaCompraProduto(
                        rs.getInt("prod_cod"),
                        rs.getInt("lc_cod"),
                        rs.getInt("lc_prod_qtd")
                );
                // As colunas no seu banco são 'prod_cod', 'lc_cod', 'lc_prod_qtd'. Ajustado aqui.
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar ListaCompraProduto: " + e.getMessage());
        }
        return null;
    }

    public List<ListaCompraProduto> get(String filtro) {
        List<ListaCompraProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM lc_prod"; // O nome da tabela é 'lc_prod'. Ajustado aqui.

        if (!filtro.isEmpty()) {
            // Replicando a concatenação direta do filtro - CUIDADO com SQL Injection!
            sql += " WHERE " + filtro;
        }

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                ListaCompraProduto lcp = new ListaCompraProduto(
                        rs.getInt("prod_cod"),
                        rs.getInt("lc_cod"),
                        rs.getInt("lc_prod_qtd")
                );
                // As colunas no seu banco são 'prod_cod', 'lc_cod', 'lc_prod_qtd'. Ajustado aqui.
                lista.add(lcp);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar ListaCompraProduto: " + e.getMessage());
        }
        return lista;
    }

    public boolean isEmpty() {
        // Replicando o uso de ResultSet e try-catch da SaidaProdDAO
        ResultSet rs = SingletonDB.getConexao().consultar("SELECT * FROM lc_prod"); // O nome da tabela é 'lc_prod'. Ajustado aqui.
        try {
            return !rs.next();
        } catch (Exception e) {
            return true;
        }
    }

    // Removido: O método 'getPorListaCompra' foi removido para replicar fielmente
    // o padrão da SaidaProdDAO fornecida, que não possuía este método.
    /*
    public List<ListaCompraProduto> getPorListaCompra(int listaCompraCod) {
        List<ListaCompraProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM lista_compra_produto WHERE lista_compra_lc_cod = " + listaCompraCod;
        // ... (lógica para consultar e preencher a lista)
        return lista;
    }
    */
}