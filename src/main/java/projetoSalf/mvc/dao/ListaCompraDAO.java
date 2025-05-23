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
        String sql = """
            INSERT INTO lista_compra(descricao, data_criacao, funcionario_id)
            VALUES ('#1', '#2', #3);
        """;
        sql = sql.replace("#1", lista.getDescricao())
                .replace("#2", lista.getDataCriacao().toString())
                .replace("#3", String.valueOf(lista.getFuncionarioId()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lista;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public ListaCompra alterar(ListaCompra lista) {
        String sql = """
            UPDATE lista_compra SET 
                descricao = '#1',
                data_criacao = '#2',
                funcionario_id = #3
            WHERE id = #4;
        """;
        sql = sql.replace("#1", lista.getDescricao())
                .replace("#2", lista.getDataCriacao().toString())
                .replace("#3", String.valueOf(lista.getFuncionarioId()))
                .replace("#4", String.valueOf(lista.getId()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return lista;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(ListaCompra lista) {
        String sql = "DELETE FROM lista_compra WHERE id = " + lista.getId();
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public ListaCompra get(int id) {
        String sql = "SELECT * FROM lista_compra WHERE id = " + id;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                ListaCompra lista = new ListaCompra();
                lista.setId(rs.getInt("id"));
                lista.setDescricao(rs.getString("descricao"));
                lista.setDataCriacao(rs.getDate("data_criacao").toLocalDate());
                lista.setFuncionarioId(rs.getInt("funcionario_id"));
                return lista;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ListaCompra> get(String filtro) {
        List<ListaCompra> listas = new ArrayList<>();
        String sql = "SELECT * FROM lista_compra";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                ListaCompra lista = new ListaCompra();
                lista.setId(rs.getInt("id"));
                lista.setDescricao(rs.getString("descricao"));
                lista.setDataCriacao(rs.getDate("data_criacao").toLocalDate());
                lista.setFuncionarioId(rs.getInt("funcionario_id"));
                listas.add(lista);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar listas: " + e.getMessage());
        }
        return listas;
    }
}
