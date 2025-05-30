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
            INSERT INTO lc_prod(lc_cod, prod_cod, lc_prod_qtd)
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
                "UPDATE lc_prod SET lc_prod_qtd = %d WHERE lc_cod = %d AND prod_cod = %d",
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
        String sql = String.format("DELETE FROM lc_prod WHERE lc_cod = %d AND prod_cod = %d",
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
        String sql = "DELETE FROM lc_prod WHERE lc_cod = " + listaId;
        return SingletonDB.getConexao().manipular(sql);
    }

    public List<Itens> getItensPorLista(int listaId) {
        List<Itens> itens = new ArrayList<>();
        String sql = "SELECT * FROM lc_prod WHERE lc_cod = " + listaId;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Itens item = new Itens();
                item.setListaId(rs.getInt("lc_cod"));
                item.setProdutoId(rs.getInt("prod_cod"));
                item.setQuantidade(rs.getInt("lc_prod_qtd"));
                itens.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar itens: " + e.getMessage());
        }
        return itens;
    }

    public Itens consultar(int listaId, int produtoId) {
        String sql = "SELECT * FROM lc_prod WHERE lc_cod = " + listaId + " AND prod_cod = " + produtoId;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                return new Itens(
                        rs.getInt("lc_cod"),
                        rs.getInt("prod_cod"),
                        rs.getInt("lc_prod_qtd")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar item: " + e.getMessage());
        }
        return null;
    }

    public Itens update(Itens item) {
        String sql = String.format(
                "UPDATE lc_prod SET lc_prod_qtd = %d WHERE lc_cod = %d AND prod_cod = %d",
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
        String sql = String.format("DELETE FROM lc_prod WHERE lc_cod = %d AND prod_cod = %d",
                item.getListaId(), item.getProdutoId());
        return SingletonDB.getConexao().manipular(sql);
    }



}