package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.CategoriaProduto;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoriaProdutoDAO {

    private final Conexao con;

    public CategoriaProdutoDAO() {
        this.con = SingletonDB.getConexao();
    }

    public boolean inserir(CategoriaProduto categoria) {
        try {
            String sql = "INSERT INTO categoria_produto (cat_desc) VALUES (?)";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, categoria.getDesc());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir categoria: " + e.getMessage());
            return false;
        }
    }

    public List<CategoriaProduto> consultarTodos() {
        List<CategoriaProduto> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM categoria_produto ORDER BY cat_cod";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriaProduto c = new CategoriaProduto();
                c.setId(rs.getInt("cat_cod"));
                c.setDesc(rs.getString("cat_desc"));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar categorias: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(CategoriaProduto categoria) {
        try {
            String sql = "UPDATE categoria_produto SET cat_desc = ? WHERE cat_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, categoria.getDesc());
            stmt.setInt(2, categoria.getId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar categoria: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int cod) {
        try {
            String sql = "DELETE FROM categoria_produto WHERE cat_cod = ?";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setInt(1, cod);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir categoria: " + e.getMessage());
            return false;
        }
    }

    public List<CategoriaProduto> buscarPorDescricao(String filtro) {
        List<CategoriaProduto> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM categoria_produto WHERE LOWER(cat_desc) LIKE LOWER(?)";
            PreparedStatement stmt = con.getConnect().prepareStatement(sql);
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriaProduto c = new CategoriaProduto();
                c.setId(rs.getInt("cat_cod"));
                c.setDesc(rs.getString("cat_desc"));
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar categoria por descrição: " + e.getMessage());
        }
        return lista;
    }
}
