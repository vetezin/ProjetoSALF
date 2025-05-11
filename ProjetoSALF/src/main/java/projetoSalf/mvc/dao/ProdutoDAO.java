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
        sql=sql.replace("#4",String.valueOf(produto.getCat_cod().getCat_cod()));

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
        sql=sql.replace("#4",String.valueOf(produto.getCat_cod().getCat_cod()));
        sql=sql.replace("#5",String.valueOf(produto.getProd_cod()));
        if (SingletonDB.getConexao().manipular(sql)) {
            return produto;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public boolean apagar(Produto produto)
    {
        return false;
    }

    @Override
    public Produto get(int id)
    {
        String sql = "SELECT * FROM empresa WHERE prod_cod = " + id;
        ResultSet resultset = SingletonDB.getConexao().consultar(sql);
        try{
            if(resultset.next())
            {
                Produto p = new Produto();
                p.setProd_cod(resultset.getLong("prod_cod"));
                p.setProd_dtvalid(resultset.getString("prod_dtvalid"));
                p.setProd_desc(resultset.getString("prod_desc"));
                p.setProd_valorun(resultset.getFloat("prod_valorun"));

                CategoriaProduto cat = new CategoriaProduto(
                        resultset.getInt("cat_cod"),
                        "" // Você pode buscar a descrição se quiser em outra consulta
                );
                p.setCat_cod(cat);

                return p;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar produto "+ e.getMessage());
        }
        return null;
    }

    @Override
    public List<Produto> get(String filtro) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        if (filtro != null && !filtro.isBlank()) {
            sql += " WHERE " + filtro;
        }
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                Produto p = new Produto();
                p.setProd_cod(rs.getLong("prod_cod"));
                p.setProd_dtvalid(rs.getString("prod_dtvalid"));
                p.setProd_desc(rs.getString("prod_desc"));
                p.setProd_valorun(rs.getFloat("prod_valorun"));

                CategoriaProduto cat = new CategoriaProduto(
                        rs.getInt("cat_cod"),
                        ""
                );
                p.setCat_cod(cat);

                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }



    public boolean isEmpty() {
        String sql = "SELECT * FROM empresa";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            return !rs.next();
        } catch (SQLException e) {
            return true;
        }
    }

    public boolean deletarProduto() {
        String sql = "DELETE FROM produto";
        return SingletonDB.getConexao().manipular(sql);
    }
}
