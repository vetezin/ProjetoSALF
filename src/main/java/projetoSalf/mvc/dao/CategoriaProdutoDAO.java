package projetoSalf.mvc.dao;

// ⬇️ Imports
import projetoSalf.mvc.model.CategoriaProduto;
import projetoSalf.mvc.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaProdutoDAO {

    private Conexao conexao;

    public CategoriaProdutoDAO() {
        conexao = new Conexao();
        conexao.conectar("jdbc:postgresql://localhost:5432/", "salf_db", "postgres", "262227380");
    }

    public void inserir(CategoriaProduto categoria) {
        String sql = "INSERT INTO categoria_produto (cat_desc) VALUES ('" + categoria.getCat_desc() + "')";
        conexao.manipular(sql);
    }

    public List<CategoriaProduto> listar() {
        List<CategoriaProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria_produto ORDER BY cat_cod";
        ResultSet rs = conexao.consultar(sql);

        try {
            while (rs.next()) {
                CategoriaProduto c = new CategoriaProduto();
                c.setCat_cod(rs.getInt("cat_cod"));
                c.setCat_desc(rs.getString("cat_desc"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }

        return lista;
    }

    public void atualizar(CategoriaProduto categoria) {
        String sql = "UPDATE categoria_produto SET cat_desc = '" + categoria.getCat_desc() + "' WHERE cat_cod = " + categoria.getCat_cod();
        conexao.manipular(sql);
    }

    public void excluir(int cat_cod) {
        String sql = "DELETE FROM categoria_produto WHERE cat_cod = " + cat_cod;
        conexao.manipular(sql);
    }

    public List<CategoriaProduto> buscarPorDescricao(String descParcial) {
        List<CategoriaProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria_produto WHERE cat_desc ILIKE '%" + descParcial + "%'";
        ResultSet rs = conexao.consultar(sql);

        try {
            while (rs.next()) {
                CategoriaProduto c = new CategoriaProduto();
                c.setCat_cod(rs.getInt("cat_cod"));
                c.setCat_desc(rs.getString("cat_desc"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categorias: " + e.getMessage());
        }

        return lista;
    }
}
