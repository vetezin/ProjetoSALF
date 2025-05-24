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
    public ListaCompra gravar(ListaCompra lista) {
        if (lista.getDataCriacao() == null || lista.getDataCriacao().isBlank()) {
            throw new IllegalArgumentException("Data de criação não pode estar vazia");
        }

        String sql = """
            INSERT INTO listacompra(lc_dtlista, lc_desc, func_cod)
            VALUES ('#1', '#2', #3);
        """;
        sql = sql.replace("#1", lista.getDataCriacao())
                .replace("#2", lista.getDescricao())
                .replace("#3", String.valueOf(lista.getFuncionarioId()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lista;
        } else {
            System.out.println("Erro ao gravar o registro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public List<ListaCompra> get(String filtro) {
        List<ListaCompra> listas = new ArrayList<>();
        String sql = "SELECT * FROM listacompra";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }

        ResultSet rs = null;
        try {
            rs = SingletonDB.getConexao().consultar(sql);

            // Verificar se o ResultSet é nulo
            if (rs == null) {
                System.out.println("Erro: O método consultar retornou um ResultSet nulo");
                return listas;
            }

            while (rs.next()) {
                ListaCompra lista = new ListaCompra();
                lista.setId(rs.getInt("lc_cod"));
                lista.setDescricao(rs.getString("lc_desc"));
                lista.setDataCriacao(rs.getString("lc_dtlista"));
                lista.setFuncionarioId(rs.getInt("func_cod"));
                listas.add(lista);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar registros: " + e.getMessage());
        } finally {
            // Fechar o ResultSet para evitar vazamento de recursos
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
                }
            }
        }
        return listas;
    }

    @Override
    public boolean apagar(ListaCompra lista) {
        String sql = "DELETE FROM listacompra WHERE lc_cod = " + lista.getId();
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public ListaCompra alterar(ListaCompra lista) {
        if (lista.getDataCriacao() == null || lista.getDataCriacao().isBlank()) {
            throw new IllegalArgumentException("Data de criação não pode estar vazia.");
        }

        String sql = """
        UPDATE listacompra SET 
            lc_desc = '#1',
            lc_dtlista = '#2',
            func_cod = #3
        WHERE lc_cod = #4;
        """;

        sql = sql.replace("#1", lista.getDescricao())
                .replace("#2", lista.getDataCriacao())
                .replace("#3", String.valueOf(lista.getFuncionarioId()))
                .replace("#4", String.valueOf(lista.getId()));

        return SingletonDB.getConexao().manipular(sql) ? lista : null;
    }

    @Override
    public ListaCompra get(int id) {
        String sql = "SELECT * FROM listacompra WHERE lc_cod = " + id;

        ResultSet rs = null;
        try {
            rs = SingletonDB.getConexao().consultar(sql);

            // Verificar se o ResultSet é nulo
            if (rs == null) {
                System.out.println("Erro: O método consultar retornou um ResultSet nulo");
                return null;
            }

            if (rs.next()) {
                ListaCompra lista = new ListaCompra();
                lista.setId(rs.getInt("lc_cod"));
                lista.setDescricao(rs.getString("lc_desc"));
                lista.setDataCriacao(rs.getString("lc_dtlista"));
                lista.setFuncionarioId(rs.getInt("func_cod"));
                return lista;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar o registro com ID " + id + ": " + e.getMessage());
        } finally {
            // Fechar o ResultSet para evitar vazamento de recursos
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
                }
            }
        }
        return null;
    }
}