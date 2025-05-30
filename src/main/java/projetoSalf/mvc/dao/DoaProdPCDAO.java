package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.DoaProd;
import projetoSalf.mvc.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

@Repository
public class DoaProdPCDAO {

    public boolean gravar(DoaProd doaProd, int doapcCod) {
        String checkEstoque = String.format(
                "SELECT es_qtdprod FROM estoque WHERE produto_prod_cod = %d",
                doaProd.getProdutoProdCod()
        );

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(checkEstoque);
            if (!rs.next()) {
                System.out.println("Produto " + doaProd.getProdutoProdCod() + " não existe no estoque.");
                return false;
            }
            int estoqueAtual = rs.getInt("es_qtdprod");
            if (estoqueAtual < doaProd.getDoaProdQtd()) {
                System.out.println("Estoque insuficiente para produto " + doaProd.getProdutoProdCod());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar estoque: " + e.getMessage());
            return false;
        }

        String sqlInsert = String.format("""
            INSERT INTO doa_prod_pc (doapc_cod, produto_prod_cod, doa_prod_qtd, doa_prod_cat_cod)
            VALUES (%d, %d, %d, %d)
        """, doapcCod, doaProd.getProdutoProdCod(), doaProd.getDoaProdQtd(), doaProd.getDoaProdCatCod());

        boolean inserido = SingletonDB.getConexao().manipular(sqlInsert);
        if (!inserido) return false;

        String sqlUpdate = String.format("""
            UPDATE estoque SET es_qtdprod = es_qtdprod - %d
            WHERE produto_prod_cod = %d
        """, doaProd.getDoaProdQtd(), doaProd.getProdutoProdCod());

        return SingletonDB.getConexao().manipular(sqlUpdate);
    }

    public boolean reverterProdutosDaDoacaoPC(int doapcCod) {
        String sql = "SELECT produto_prod_cod, doa_prod_qtd FROM doa_prod_pc WHERE doapc_cod = " + doapcCod;

        try {
            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            while (rs.next()) {
                int prodCod = rs.getInt("produto_prod_cod");
                int qtd = rs.getInt("doa_prod_qtd");

                String updateEstoque = String.format("""
                    UPDATE estoque SET es_qtdprod = es_qtdprod + %d
                    WHERE produto_prod_cod = %d
                """, qtd, prodCod);

                boolean ok = SingletonDB.getConexao().manipular(updateEstoque);
                if (!ok) {
                    System.out.println("Erro ao atualizar estoque do produto " + prodCod);
                    return false;
                }
            }

            String delete = "DELETE FROM doa_prod_pc WHERE doapc_cod = " + doapcCod;
            return SingletonDB.getConexao().manipular(delete);

        } catch (Exception e) {
            System.out.println("Erro ao reverter produtos da doacao_pc:");
            e.printStackTrace();
            return false;
        }
    }
}
