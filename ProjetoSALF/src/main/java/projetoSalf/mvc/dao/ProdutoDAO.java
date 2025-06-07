/*
package projetoSalf.mvc.dao;


import projetoSalf.mvc.model.Categoria;



import org.springframework.stereotype.Repository;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.model.Produto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO implements IDAO<Produto>
{

    @Override
    public Produto gravar(Produto produto) {
        String sql = """
                INSERT INTO produto(prod_dtvalid, prod_desc, prod_valorun, cat_cod) 
                VALUES ('#1', '#2', '#3', '#4');
        """;

        sql=sql.replace("#1",produto.getProd_dtvalid());
        sql=sql.replace("#2",produto.getProd_desc());
        sql=sql.replace("#3",String.valueOf(produto.getProd_valorun()));
        sql=sql.replace("#4",String.valueOf(produto.getCategoria()));

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


        sql = sql.replace("#1", "'" + produto.getProd_dtvalid() + "'");
        sql = sql.replace("#2", "'" + produto.getProd_desc().replace("'", "''") + "'");
        sql = sql.replace("#3", String.valueOf(produto.getProd_valorun()));
        sql = sql.replace("#4", String.valueOf(produto.getCategoria()));
        sql = sql.replace("#5", String.valueOf(produto.getProd_cod()));
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
                        resultSet.getInt("prod_cod"),
                        resultSet.getString("prod_dtvalid"),
                        resultSet.getString("prod_desc"),
                        resultSet.getFloat("prod_valorun"),
                       resultSet.getInt("cat_cod")

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
                        rs.getInt("prod_cod"),
                        rs.getString("prod_dtvalid"),
                        rs.getString("prod_desc"),
                        rs.getFloat("prod_valorun"),
                        rs.getInt("cat_cod")

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

    @Override
    public boolean apagar(Produto produto) {
        if (produto == null)
            return false;

        String sql = "DELETE FROM produto WHERE prod_cod = " + produto.getProd_cod();
        return SingletonDB.getConexao().manipular(sql);
    }
}
*/
package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Categoria;
import org.springframework.stereotype.Repository;
import projetoSalf.mvc.util.SingletonDB;
import projetoSalf.mvc.model.Produto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoDAO implements IDAO<Produto> {

    @Override
    public Produto gravar(Produto produto) {
        String sql = """
                INSERT INTO produto(prod_desc, prod_valorun, cat_cod) 
                VALUES ('#1', '#2', '#3');
        """;

        sql = sql.replace("#1", produto.getProd_desc().replace("'", "''"));
        sql = sql.replace("#2", String.valueOf(produto.getProd_valorun()));
        sql = sql.replace("#3", String.valueOf(produto.getCategoria()));

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
            prod_desc = '#1',
            prod_valorun = #2,
            cat_cod = #3
            WHERE prod_cod = #4;
        """;

        sql = sql.replace("#1", produto.getProd_desc().replace("'", "''"));
        sql = sql.replace("#2", String.valueOf(produto.getProd_valorun()));
        sql = sql.replace("#3", String.valueOf(produto.getCategoria()));
        sql = sql.replace("#4", String.valueOf(produto.getProd_cod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return produto;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Produto get(int id) {
        String sql = "SELECT * FROM produto WHERE prod_cod = " + id;
        Produto p = null;
        try {
            ResultSet resultSet = SingletonDB.getConexao().consultar(sql);
            if (resultSet.next()) {
                p = new Produto(
                        resultSet.getInt("prod_cod"),
                        resultSet.getString("prod_desc"),
                        resultSet.getFloat("prod_valorun"),
                        resultSet.getInt("cat_cod")
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
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
                        rs.getInt("prod_cod"),
                        rs.getString("prod_desc"),
                        rs.getFloat("prod_valorun"),
                        rs.getInt("cat_cod")
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

    @Override
    public boolean apagar(Produto produto) {
        if (produto == null)
            return false;

        String sql = "DELETE FROM produto WHERE prod_cod = " + produto.getProd_cod();
        return SingletonDB.getConexao().manipular(sql);
    }
}
