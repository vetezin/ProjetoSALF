package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.Estoque;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EstoqueDAO implements IDAO<Estoque> {

    @Override
    public Estoque gravar(Estoque estoque) {
        String sql = """
        INSERT INTO estoque(es_qtdprod, es_dtvalidade, produto_prod_cod)
        VALUES (#1, '#2', #3);
        """;
        sql = sql.replace("#1", String.valueOf(estoque.getEs_qtdprod()));
        sql = sql.replace("#2", estoque.getEs_dtvalidade());
        sql = sql.replace("#3", String.valueOf(estoque.getProduto_prod_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return estoque;
        } else {
            System.out.println("Erro ao inserir estoque: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Estoque alterar(Estoque estoque) {
        String sql = """
        UPDATE estoque SET
        es_qtdprod = #1,
        es_dtvalidade = '#2',
        produto_prod_cod = #3
        WHERE estoque_id = #4;
        """;
        sql = sql.replace("#1", String.valueOf(estoque.getEs_qtdprod()));
        sql = sql.replace("#2", estoque.getEs_dtvalidade());
        sql = sql.replace("#3", String.valueOf(estoque.getProduto_prod_cod()));
        sql = sql.replace("#4", String.valueOf(estoque.getEstoque_id()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return estoque;
        } else {
            System.out.println("Erro ao alterar estoque: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Estoque get(int id) {
        String sql = "SELECT * FROM estoque WHERE estoque_id = " + id;
        Estoque estoque = null;

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                estoque = new Estoque(
                        rs.getInt("estoque_id"),
                        rs.getInt("es_qtdprod"),
                        rs.getString("es_dtvalidade"),
                        rs.getInt("produto_prod_cod")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar estoque por ID: " + e.getMessage());
        }

        return estoque;
    }

    @Override
    public List<Estoque> get(String filtro) {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM estoque";

        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                Estoque e = new Estoque(
                        rs.getInt("estoque_id"),
                        rs.getInt("es_qtdprod"),
                        rs.getString("es_dtvalidade"),
                        rs.getInt("produto_prod_cod")
                );
                lista.add(e);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar estoques: " + e.getMessage());
        }

        return lista;
    }

    // NOVO MÉTODO: Lista estoque com informações completas do produto e categoria
    public List<Object[]> getEstoqueCompleto() {
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT 
                e.estoque_id,
                e.es_qtdprod,
                e.es_dtvalidade,
                e.produto_prod_cod,
                p.prod_desc as produto_nome,
                c.cat_desc as categoria_nome
            FROM estoque e
            INNER JOIN produto p ON e.produto_prod_cod = p.prod_cod
            LEFT JOIN categoria_produto c ON p.cat_cod = c.cat_cod
            ORDER BY e.estoque_id
        """;

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                Object[] item = new Object[6];
                item[0] = rs.getInt("estoque_id");           // estoque_id
                item[1] = rs.getInt("es_qtdprod");           // quantidade
                item[2] = rs.getString("es_dtvalidade");     // validade
                item[3] = rs.getInt("produto_prod_cod");     // produto_id
                item[4] = rs.getString("produto_nome");      // produto_nome
                item[5] = rs.getString("categoria_nome");    // categoria_nome
                lista.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar estoque completo: " + e.getMessage());
        }

        return lista;
    }

    // MÉTODO ALTERNATIVO: Se quiser retornar um Map mais estruturado
    public List<java.util.Map<String, Object>> getEstoqueCompletoMap() {
        List<java.util.Map<String, Object>> lista = new ArrayList<>();
        String sql = """
            SELECT 
                e.estoque_id,
                e.es_qtdprod,
                e.es_dtvalidade,
                e.produto_prod_cod,
                p.prod_desc as produto_nome,
                c.cat_desc as categoria_nome
            FROM estoque e
            INNER JOIN produto p ON e.produto_prod_cod = p.prod_cod
            LEFT JOIN categoria_produto c ON p.cat_cod = c.cat_cod
            ORDER BY e.estoque_id
        """;

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                java.util.Map<String, Object> item = new java.util.HashMap<>();

                // Dados do estoque
                item.put("id", rs.getInt("estoque_id"));
                item.put("quantidade", rs.getInt("es_qtdprod"));
                item.put("validade", rs.getString("es_dtvalidade"));
                item.put("produtoId", rs.getInt("produto_prod_cod"));

                // Dados do produto (estrutura aninhada como o JSON atual)
                java.util.Map<String, Object> produto = new java.util.HashMap<>();
                produto.put("id", rs.getInt("produto_prod_cod"));
                produto.put("nome", rs.getString("produto_nome"));
                produto.put("categoria", rs.getString("categoria_nome"));

                item.put("produto", produto);
                lista.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar estoque completo: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean apagar(Estoque estoque) {
        if (estoque == null) return false;

        String sql = "DELETE FROM estoque WHERE estoque_id = " + estoque.getEstoque_id();
        return SingletonDB.getConexao().manipular(sql);
    }

    public boolean isEmpty() {
        String sql = "SELECT * FROM estoque";
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }
}