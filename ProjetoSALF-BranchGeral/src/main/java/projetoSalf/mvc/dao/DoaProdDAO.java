package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.DoaProd;
import projetoSalf.mvc.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoaProdDAO {
    public boolean gravar(DoaProd doaProd) {
        String sql = String.format(
                "INSERT INTO doa_prod(doa_prod_qtd, doa_prod_catcod, produto_prod_cod, doacao_doa_cod) " +
                        "VALUES (%d, %d, %d, %d)",
                doaProd.getDoaProdQtd(), doaProd.getDoaProdCatCod(),
                doaProd.getProdutoProdCod(), doaProd.getDoacaoDoaCod()
        );

        boolean sucesso = SingletonDB.getConexao().manipular(sql);
        if (sucesso) {
            // Primeiro tenta atualizar o estoque
            String sqlEstoqueUpdate = String.format(
                    "UPDATE estoque SET es_qtdprod = es_qtdprod + %d WHERE produto_prod_cod = %d",
                    doaProd.getDoaProdQtd(), doaProd.getProdutoProdCod()
            );
            boolean updateOk = SingletonDB.getConexao().manipular(sqlEstoqueUpdate);

            if (!updateOk) {
                // Se não atualizou (provavelmente porque não existe), insere o produto no estoque
                String sqlEstoqueInsert = String.format(
                        "INSERT INTO estoque (produto_prod_cod, es_qtdprod) VALUES (%d, %d)",
                        doaProd.getProdutoProdCod(), doaProd.getDoaProdQtd()
                );
                return SingletonDB.getConexao().manipular(sqlEstoqueInsert);
            }

            return true;
        }

        return false;
    }

    public List<DoaProd> listarPorDoacao(int doaCod) {
        List<DoaProd> lista = new ArrayList<>();
        String sql = "SELECT * FROM doa_prod WHERE doacao_doa_cod = " + doaCod;

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                DoaProd dp = new DoaProd();
                dp.setDoaProdCod(rs.getInt("doa_prod_cod"));
                dp.setDoaProdQtd(rs.getInt("doa_prod_qtd"));
                dp.setDoaProdCatCod(rs.getInt("doa_prod_catcod"));
                dp.setProdutoProdCod(rs.getInt("produto_prod_cod"));
                dp.setDoacaoDoaCod(rs.getInt("doacao_doa_cod"));
                lista.add(dp);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar doa_prod: " + e.getMessage());
        }

        return lista;
    }

    public boolean reverterDoacao(int doaCod) {
        String sql = "SELECT * FROM doa_prod WHERE doacao_doa_cod = " + doaCod;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                int prodCod = rs.getInt("produto_prod_cod");
                int qtd = rs.getInt("doa_prod_qtd");

                String updateEstoque = String.format(
                        "UPDATE estoque SET es_qtdprod = es_qtdprod - %d WHERE produto_prod_cod = %d",
                        qtd, prodCod
                );
                SingletonDB.getConexao().manipular(updateEstoque);
            }

            String deleteDoaProd = "DELETE FROM doa_prod WHERE doacao_doa_cod = " + doaCod;
            return SingletonDB.getConexao().manipular(deleteDoaProd);

        } catch (Exception e) {
            System.out.println("Erro ao reverter produtos da doação: " + e.getMessage());
            return false;
        }
    }

}
