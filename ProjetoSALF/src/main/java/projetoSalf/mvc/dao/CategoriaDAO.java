package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.Categoria;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoriaDAO implements IDAO<Categoria> {


    @Override
    public Categoria gravar(Categoria categoria) {
        String sql = """
                INSERT INTO categoria_produto(cat_desc) 
                VALUES ('#1');
        """;
        sql = sql.replace("#1", categoria.getDesc());

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            if (rs.next()) {
                categoria.setId(rs.getInt("cat_cod"));
                return categoria;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao gravar categoria: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Categoria alterar(Categoria categoria) {
        String sql = """
                UPDATE categoria_produto SET cat_desc = '#1' 
                WHERE cat_cod = #2;
        """;
        sql = sql.replace("#1", categoria.getDesc());
        sql = sql.replace("#2", String.valueOf(categoria.getId()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return categoria;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Categoria get(int id) {

        String sql = "SELECT * FROM categoria_produto WHERE cat_cod = " + id;
        try {
            System.out.println("aqui");
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {

                return new Categoria(rs.getInt("cat_cod"), rs.getString("cat_desc"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Categoria> get(String filtro) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categoria_produto";
        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {

            while (rs.next()) {
                Categoria c = new Categoria(
                        rs.getInt("cat_cod"),
                        rs.getString("cat_desc")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar categorias: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean apagar(Categoria categoria) {
        if (categoria == null) return false;
        String sql = "DELETE FROM categoria_produto WHERE cat_cod = " + categoria.getId();
        return SingletonDB.getConexao().manipular(sql);
    }
}
