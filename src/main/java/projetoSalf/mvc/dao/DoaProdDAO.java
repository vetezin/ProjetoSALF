package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.DoaProd;
import projetoSalf.mvc.util.SingletonDB;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoaProdDAO {

    public boolean gravar(DoaProd doaProd) {
        try {
            // Primeiro insere o produto da doação
            String sql = String.format("""
            INSERT INTO doa_prod(doa_prod_qtd, doa_prod_catcod, produto_prod_cod, doacao_doa_cod)
            VALUES (%d, %d, %d, %d);
        """, doaProd.getDoaProdQtd(), doaProd.getDoaProdCatCod(),
                    doaProd.getProdutoProdCod(), doaProd.getDoacaoDoaCod());

            boolean sucesso = SingletonDB.getConexao().manipular(sql);

            if (!sucesso) return false;

            // Agora atualiza o estoque
            int qtd = doaProd.getDoaProdQtd();
            int prodCod = doaProd.getProdutoProdCod();

            // Verifica se já existe registro no estoque
            String verificaEstoque = "SELECT * FROM estoque WHERE produto_prod_cod = " + prodCod;
            ResultSet rs = SingletonDB.getConexao().consultar(verificaEstoque);

            if (rs.next()) {
                // Produto já tem estoque, atualiza somando
                String updateEstoque = String.format("""
                UPDATE estoque SET es_qtdprod = COALESCE(es_qtdprod, 0) + %d
                WHERE produto_prod_cod = %d;
            """, qtd, prodCod);
                return SingletonDB.getConexao().manipular(updateEstoque);
            } else {
                // Produto ainda não tem estoque, insere novo
                String insertEstoque = String.format("""
                INSERT INTO estoque (produto_prod_cod, es_qtdprod)
                VALUES (%d, %d);
            """, prodCod, qtd);
                return SingletonDB.getConexao().manipular(insertEstoque);
            }

        } catch (Exception e) {
            System.out.println("Erro ao gravar doacao_prod: " + e.getMessage());
            return false;
        }
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
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos da doação: " + e.getMessage());
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

                String updateEstoque = String.format("""
                    UPDATE estoque SET es_qtdprod = es_qtdprod - %d
                    WHERE produto_prod_cod = %d;
                """, qtd, prodCod);

                SingletonDB.getConexao().manipular(updateEstoque);
            }

            String deleteDoaProd = "DELETE FROM doa_prod WHERE doacao_doa_cod = " + doaCod;
            return SingletonDB.getConexao().manipular(deleteDoaProd);

        } catch (SQLException e) {
            System.out.println("Erro ao reverter produtos da doação: " + e.getMessage());
            return false;
        }
    }

    public boolean reverterProdutosDaDoacaoPC(int doapcCod) {
        String sql = "SELECT * FROM doa_prod_pc WHERE doapc_cod = " + doapcCod;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);

        try {
            while (rs.next()) {
                int prodCod = rs.getInt("produto_cod");
                int qtd = rs.getInt("qtd");

                String updateEstoque = String.format("""
                    UPDATE estoque SET es_qtdprod = es_qtdprod + %d
                    WHERE produto_prod_cod = %d;
                """, qtd, prodCod);

                SingletonDB.getConexao().manipular(updateEstoque);
            }

            String delete = "DELETE FROM doa_prod_pc WHERE doapc_cod = " + doapcCod;
            return SingletonDB.getConexao().manipular(delete);
        } catch (SQLException e) {
            System.out.println("Erro ao reverter produtos da doacao_pc: " + e.getMessage());
            return false;
        }
    }
}