package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.SaidaProd;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SaidaProdDAO {

    public SaidaProd gravar(SaidaProd sp) {
        String sql = """
            INSERT INTO saida_prod (produto_prod_cod, saida_s_cod, sp_qtd)
            VALUES (#1, #2, #3);
        """;
        sql = sql.replace("#1", String.valueOf(sp.getProdutoCod()));
        sql = sql.replace("#2", String.valueOf(sp.getSaidaCod()));
        sql = sql.replace("#3", String.valueOf(sp.getQtd()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return sp;
        } else {
            System.out.println("Erro ao inserir SaidaProd: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    public SaidaProd alterar(SaidaProd sp) {
        String sql = """
            UPDATE saida_prod
            SET sp_qtd = #3
            WHERE produto_prod_cod = #1 AND saida_s_cod = #2;
        """;
        sql = sql.replace("#1", String.valueOf(sp.getProdutoCod()));
        sql = sql.replace("#2", String.valueOf(sp.getSaidaCod()));
        sql = sql.replace("#3", String.valueOf(sp.getQtd()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return sp;
        } else {
            System.out.println("Erro ao atualizar SaidaProd: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    public boolean apagar(SaidaProd sp) {
        String sql = String.format(
                "DELETE FROM saida_prod WHERE produto_prod_cod = %d AND saida_s_cod = %d",
                sp.getProdutoCod(), sp.getSaidaCod()
        );
        return SingletonDB.getConexao().manipular(sql);
    }

    public SaidaProd get(int produtoCod, int saidaCod) {
        String sql = String.format(
                "SELECT * FROM saida_prod WHERE produto_prod_cod = %d AND saida_s_cod = %d",
                produtoCod, saidaCod
        );
        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (rs.next()) {
                return new SaidaProd(
                        rs.getInt("produto_prod_cod"),
                        rs.getInt("saida_s_cod"),
                        rs.getInt("sp_qtd")
                );
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar SaidaProd: " + e.getMessage());
        }
        return null;
    }

    public List<SaidaProd> get(String filtro) {
        List<SaidaProd> lista = new ArrayList<>();
        String sql = "SELECT * FROM saida_prod";
        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                SaidaProd sp = new SaidaProd(
                        rs.getInt("produto_prod_cod"),
                        rs.getInt("saida_s_cod"),
                        rs.getInt("sp_qtd")
                );
                lista.add(sp);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar SaidaProd: " + e.getMessage());
        }
        return lista;
    }

    public boolean isEmpty() {
        ResultSet rs = SingletonDB.getConexao().consultar("SELECT * FROM saida_prod");
        try {
            return !rs.next();
        } catch (Exception e) {
            return true;
        }
    }
}
