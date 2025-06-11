package projetoSalf.mvc.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.DoacaoPC;
import projetoSalf.mvc.model.DoaProd;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoacaoPCDAO {

    @Autowired
    private DoaProdPCDAO prodPCDAO;

    public boolean inserir(DoacaoPC doacao) {
        try {
            System.out.println("Iniciando inserção de doação...");

            for (DoaProd p : doacao.getProdutos()) {
                String sqlCheck = String.format(
                        "SELECT COUNT(*) FROM estoque WHERE produto_prod_cod = %d",
                        p.getProdutoProdCod()
                );
                ResultSet rsCheck = SingletonDB.getConexao().consultar(sqlCheck);
                if (rsCheck.next() && rsCheck.getInt(1) == 0) {
                    System.out.printf("Produto %d não existe no estoque. Doação abortada.\n", p.getProdutoProdCod());
                    return false;
                }
            }

            String sql = String.format(
                    "INSERT INTO doacao_pc(func_cod, pc_cod, doapc_data) VALUES (%d, %d, '%s') RETURNING doapc_cod",
                    doacao.getFuncCod(), doacao.getPcCod(), doacao.getDoapcData()
            );
            System.out.println("SQL DOACAO_PC: " + sql);

            ResultSet rs = SingletonDB.getConexao().consultar(sql);
            if (!rs.next()) {
                System.out.println("Erro ao obter doapc_cod gerado.");
                return false;
            }

            int cod = rs.getInt("doapc_cod");
            System.out.println("Doação registrada com cod: " + cod);
            doacao.setDoapcCod(cod);

            // GRAVA PRODUTOS
            for (DoaProd p : doacao.getProdutos()) {

                // VERIFICA ESTOQUE
                String sqlCheck = String.format(
                        "SELECT es_qtdprod FROM estoque WHERE produto_prod_cod = %d",
                        p.getProdutoProdCod()
                );
                ResultSet rsEstoque = SingletonDB.getConexao().consultar(sqlCheck);
                if (rsEstoque.next()) {
                    int qtdAtual = rsEstoque.getInt("es_qtdprod");
                    if (qtdAtual < p.getDoaProdQtd()) {
                        System.out.printf("Estoque insuficiente para o produto %d. Quantidade disponível: %d, necessária: %d\n",
                                p.getProdutoProdCod(), qtdAtual, p.getDoaProdQtd());
                        return false;
                    }
                } else {
                    System.out.printf("Produto %d não existe no estoque.\n", p.getProdutoProdCod());
                    return false;
                }

                // INSERE PRODUTO NA doa_prod_pc
                String insertProd = String.format(
                        "INSERT INTO doa_prod_pc (doapc_cod, produto_prod_cod, doa_prod_qtd, doa_prod_cat_cod) VALUES (%d, %d, %d, %d)",
                        cod, p.getProdutoProdCod(), p.getDoaProdQtd(), p.getDoaProdCatCod()
                );
                System.out.println("SQL INSERT PRODUTO: " + insertProd);

                if (!SingletonDB.getConexao().manipular(insertProd)) {
                    System.out.println("Erro ao inserir produto da doação.");
                    return false;
                }

                // ATUALIZA ESTOQUE
                String sqlEstoqueUpdate = String.format(
                        "UPDATE estoque SET es_qtdprod = es_qtdprod - %d WHERE produto_prod_cod = %d",
                        p.getDoaProdQtd(), p.getProdutoProdCod()
                );
                System.out.println("SQL UPDATE ESTOQUE: " + sqlEstoqueUpdate);
                SingletonDB.getConexao().manipular(sqlEstoqueUpdate);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Erro ao inserir doacao_pc: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public List<DoacaoPC> listarTodos() {
        List<DoacaoPC> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_pc ORDER BY doapc_data DESC";
        ResultSet rs = SingletonDB.getConexao().consultar(sql);

        try {
            while (rs.next()) {
                DoacaoPC d = new DoacaoPC();
                d.setDoapcCod(rs.getInt("doapc_cod"));
                d.setFuncCod(rs.getInt("func_cod"));
                d.setPcCod(rs.getInt("pc_cod"));
                d.setDoapcData(rs.getDate("doapc_data").toLocalDate());

                // Aqui adiciona os produtos à doação
                d.setProdutos(prodPCDAO.buscarProdutosPorDoacaoPC(d.getDoapcCod()));

                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar doacao_pc: " + e.getMessage());
        }

        return lista;
    }

    public List<DoacaoPC> listarPorFuncionario(int funcCod) {
        List<DoacaoPC> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_pc WHERE func_cod = " + funcCod + " ORDER BY doapc_data DESC";

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                DoacaoPC d = new DoacaoPC();
                d.setDoapcCod(rs.getInt("doapc_cod"));
                d.setFuncCod(rs.getInt("func_cod"));
                d.setPcCod(rs.getInt("pc_cod"));
                d.setDoapcData(rs.getDate("doapc_data").toLocalDate());

                // Aqui adiciona os produtos à doação
                d.setProdutos(prodPCDAO.buscarProdutosPorDoacaoPC(d.getDoapcCod()));

                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar doações por funcionário: " + e.getMessage());
        }

        return lista;
    }

    public List<DoacaoPC> listarPorPessoaCarente(int pcCod) {
        List<DoacaoPC> lista = new ArrayList<>();
        String sql = "SELECT * FROM doacao_pc WHERE pc_cod = " + pcCod + " ORDER BY doapc_data DESC";

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        try {
            while (rs.next()) {
                DoacaoPC d = new DoacaoPC();
                d.setDoapcCod(rs.getInt("doapc_cod"));
                d.setFuncCod(rs.getInt("func_cod"));
                d.setPcCod(rs.getInt("pc_cod"));
                d.setDoapcData(rs.getDate("doapc_data").toLocalDate());

                // Aqui adiciona os produtos à doação
                d.setProdutos(prodPCDAO.buscarProdutosPorDoacaoPC(d.getDoapcCod()));

                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar doações por pessoa carente: " + e.getMessage());
        }

        return lista;
    }


    public DoacaoPC buscarPorId(int id) {
        String sql = "SELECT * FROM doacao_pc WHERE doapc_cod = " + id;
        ResultSet rs = SingletonDB.getConexao().consultar(sql);

        try {
            if (rs.next()) {
                DoacaoPC d = new DoacaoPC();
                d.setDoapcCod(rs.getInt("doapc_cod"));
                d.setFuncCod(rs.getInt("func_cod"));
                d.setPcCod(rs.getInt("pc_cod"));
                d.setDoapcData(rs.getDate("doapc_data").toLocalDate());
                return d;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar doacao_pc: " + e.getMessage());
        }

        return null;
    }

    public boolean deletar(int cod) {
        try {
            boolean revertido = prodPCDAO.reverterProdutosDaDoacaoPC(cod);

            if (!revertido) {
                System.out.println("Aviso: nenhum produto foi revertido para doapc_cod = " + cod + ". Continuando exclusão da doacao_pc.");
            }

            String sql = "DELETE FROM doacao_pc WHERE doapc_cod = " + cod;
            return SingletonDB.getConexao().manipular(sql);

        } catch (Exception e) {
            System.out.println("Erro ao deletar doacao_pc:");
            e.printStackTrace();
            return false;
        }
    }

    public int getMaxPK() {
        int max = 0;
        try {
            ResultSet rs = SingletonDB.getConexao().consultar("SELECT MAX(doapc_cod) FROM doacao_pc");
            if (rs.next()) max = rs.getInt(1);
        } catch (Exception e) {
            System.out.println("Erro ao buscar MAX PK de doacao_pc: " + e.getMessage());
        }
        return max;
    }

}