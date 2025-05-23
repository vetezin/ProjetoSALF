package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.Itens;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ItensDAO implements IDAO<Itens>{

    public Itens gravar(Itens item) {
        String sql = """
            INSERT INTO lista_compra_prod(lista_id, produto_id, quantidade)
            VALUES (#1, #2, #3);
        """;
        sql = sql.replace("#1", String.valueOf(item.getListaId()))
                .replace("#2", String.valueOf(item.getProdutoId()))
                .replace("#3", String.valueOf(item.getQuantidade()));
        if (SingletonDB.getConexao().manipular(sql)) {
            return item;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Object alterar(Itens item) {
        String sql = String.format(
                "UPDATE lista_compra_prod SET quantidade = %d WHERE lista_id = %d AND produto_id = %d",
                item.getQuantidade(), item.getListaId(), item.getProdutoId()
        );
        if (SingletonDB.getConexao().manipular(sql)) {
            return item;
        } else {
            System.out.println("Erro ao atualizar item: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Itens entidade) {
        String sql = String.format("DELETE FROM lista_compra_prod WHERE lista_id = %d AND produto_id = %d",
                entidade.getListaId(), entidade.getProdutoId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public Itens get(int id) {
        return null;
    }

    @Override
    public List<Itens> get(String filtro) {
        return List.of();
    }

    public boolean apagarPorLista(int listaId) {
        String sql = "DELETE FROM lista_compra_prod WHERE lista_id = " + listaId;
        return SingletonDB.getConexao().manipular(sql);
    }

    public List<Itens> getItensPorLista(int listaId) {
        List<Itens> itens = new ArrayList<>();
        String sql = "SELECT * FROM lista_compra_prod WHERE lista_id = " + listaId;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Itens item = new Itens();
                item.setListaId(rs.getInt("lista_id"));
                item.setProdutoId(rs.getInt("produto_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                itens.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar itens: " + e.getMessage());
        }
        return itens;
    }

    public Itens consultar(int listaId, int produtoId) {
        String sql = "SELECT * FROM lista_compra_prod WHERE lista_id = " + listaId + " AND produto_id = " + produtoId;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new Itens(
                        rs.getInt("lista_id"),
                        rs.getInt("produto_id"),
                        rs.getInt("quantidade")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar item: " + e.getMessage());
        }
        return null;
    }

    public Itens update(Itens item) {
        String sql = String.format(
                "UPDATE lista_compra_prod SET quantidade = %d WHERE lista_id = %d AND produto_id = %d",
                item.getQuantidade(), item.getListaId(), item.getProdutoId()
        );
        if (SingletonDB.getConexao().manipular(sql)) {
            return item;
        } else {
            System.out.println("Erro ao atualizar item: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    public boolean deletar(Itens item) {
        String sql = String.format("DELETE FROM lista_compra_prod WHERE lista_id = %d AND produto_id = %d",
                item.getListaId(), item.getProdutoId());
        return SingletonDB.getConexao().manipular(sql);
    }



}
