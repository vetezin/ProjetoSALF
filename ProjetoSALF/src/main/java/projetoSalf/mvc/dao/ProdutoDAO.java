package projetoSalf.mvc.dao;


import projetoSalf.mvc.model.CategoriaProduto;


import org.springframework.stereotype.Repository;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.model.Produto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO implements IDAO<Produto>{

    @Override
    public Produto gravar(Produto produto) {
        String sql = """
                INSERT INTO produto(prod_dtvalid, prod_desc, prod_valorun, cat_cod) 
                VALUES ('#1', '#2', '#3', '#4');
        """;

        sql=sql.replace("#1",produto.getProd_dtvalid());
        sql=sql.replace("#2",produto.getProd_desc());
        sql=sql.replace("#3",String.valueOf(produto.getProd_valorun()));
        sql=sql.replace("#4",String.valueOf(produto.getCategoria().getCat_ID()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return produto;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Produto alterar(Produto produto) {
        String sql = """
            UPDATE produto SET
            prod_dtvalid=#1,
             prod_desc=#2,
             prod_valorun=#3,
             cat_cod=#4
            WHERE prod_cod = #5;
            
        """;

        sql=sql.replace("#1",produto.getProd_dtvalid());
        sql=sql.replace("#2",produto.getProd_desc());
        sql=sql.replace("#3",String.valueOf(produto.getProd_valorun()));
        sql=sql.replace("#4",String.valueOf(produto.getCategoria().getCat_ID()));
        sql=sql.replace("#5",String.valueOf(produto.getProd_cod()));
        if (SingletonDB.getConexao().manipular(sql)) {
            return produto;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }



    @Override
    public Produto get(int id)
    {
        String sql = "SELECT * FROM produto WHERE prod_cod = " + id;
        Produto p = null;
        try{
            ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
            if(resultSet.next())
            {
               p = new Produto(
                        resultSet.getLong("prod_cod"),
                        resultSet.getString("prod_dtvalid"),
                        resultSet.getString("prod_desc"),
                        resultSet.getFloat("prod_valorun"),
                        new Categoria(resultSet.getLong("cat_cod"))

                );

            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar produto "+ e.getMessage());
        }
        return p;
    }

    @Override
    public List<Produto> get(String filtro) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
               Produto p = new Produto(
                        rs.getLong("prod_cod"),
                        rs.getString("prod_dtvalid"),
                        rs.getString("prod_desc"),
                        rs.getFloat("prod_valorun"),
                        new Categoria(rs.getLong("cat_cod"))

                );

                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }



    public boolean isEmpty() {
        String sql = "SELECT * FROM produto";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean deletarProduto(Long id) {
        String sql = "DELETE FROM produto where prod_cod= "+id;
        return SingletonDB.getConexao().manipular(sql);
    }
}
