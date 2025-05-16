package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.CategoriaProduto;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaProdutoDAO implements IDAO<CategoriaProduto> {

    @Override
    public CategoriaProduto gravar(CategoriaProduto catProd) {

        String SQL = "INSERT INTO categoria_produto (cat_desc) VALUES (?)";
        try{
             Conexao conexao = new Conexao();
             Connection con = conexao.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL);
             stmt.setString(1, catProd.getCatDesc());

             if(stmt.executeUpdate()>0){
                 conexao.fechar();
                 return catProd;
             }

        }catch (Exception e){
        }
        return null;
    }

    @Override
    public CategoriaProduto alterar(CategoriaProduto catProd) {

        String SQL = "UPDATE categoria_produto SET cat_desc = ? WHERE cat_cod = ?";

        try{
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setString(1, catProd.getCatDesc());
            stmt.setInt(2, catProd.getCatCod());

            if(stmt.executeUpdate()>0){
                conexao.fechar();
                return catProd;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean apagar(CategoriaProduto catProd) {
        String SQL = "DELETE FROM categoria_produto WHERE cat_cod = ?";


        try{

            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setInt(1, catProd.getCatCod());

            if(stmt.executeUpdate()>0){
                conexao.fechar();
                return true;
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoriaProduto get(int id) {
        String SQL = "SELECT * FROM categoria_produto WHERE cat_cod = ?";


        try{
            Conexao conexao = new Conexao();
            Connection con = conexao.getConnection();
            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                CategoriaProduto catProd = new CategoriaProduto();
                catProd.setCatDesc(rs.getString("cat_desc"));
                return catProd;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public List<CategoriaProduto> get(String filtro) {
        String SQL = "SELECT * FROM categoria_produto WHERE cat_desc LIKE ?";
        List<CategoriaProduto> lista = new ArrayList<>();

        SingletonDB.conectar();
        try (Connection conn = SingletonDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {

            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CategoriaProduto catProd = new CategoriaProduto();
                catProd.setCatCod(rs.getInt("cat_cod"));
                catProd.setCatDesc(rs.getString("cat_desc"));
                lista.add(catProd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
