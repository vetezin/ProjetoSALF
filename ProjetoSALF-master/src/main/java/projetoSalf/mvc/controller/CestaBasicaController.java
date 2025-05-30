package projetoSalf.mvc.controller;

import projetoSalf.mvc.dao.CestaBasicaDAO;
import projetoSalf.mvc.dao.CbProdDAO;
<<<<<<< HEAD
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;

import java.sql.Connection;
=======
import projetoSalf.mvc.dao.EstoqueCestaDAO;
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;

>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
import java.util.List;

public class CestaBasicaController {

<<<<<<< HEAD
    public boolean registrarCesta(CestaBasica cesta, List<CbProd> listaProdutos, Connection conn) {
        try {
            CestaBasicaDAO cestaDAO = new CestaBasicaDAO();
            int idCesta = cestaDAO.inserir(cesta, conn);

            CbProdDAO cbProdDAO = new CbProdDAO();
            for (CbProd prod : listaProdutos) {
                prod.setCestaBasicaCbCod(idCesta);
                cbProdDAO.inserir(prod, conn);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Erro ao salvar a cesta básica: " + e.getMessage());
            return false;
        }
=======
    public boolean registrarCesta(CestaBasica cesta, List<CbProd> listaProdutos, Conexao conexao) {
        CestaBasicaDAO cestaDAO = new CestaBasicaDAO();
        CbProdDAO cbProdDAO = new CbProdDAO();
        EstoqueCestaDAO estoqueDAO = new EstoqueCestaDAO();

        boolean cestaSalva = cestaDAO.inserir(cesta, conexao);
        if (!cestaSalva) {
            System.err.println("Erro ao salvar a cesta básica.");
            return false;
        }

        int idCesta = conexao.getMaxPK("cesta_basica", "cb_cod");

        for (CbProd prod : listaProdutos) {
            prod.setCestaBasicaCbCod(idCesta);

            int novoId = conexao.getMaxPK("cb_prod", "cb_prod_id") + 1;
            prod.setCbProdId(novoId);

            boolean produtoSalvo = cbProdDAO.inserir(prod, conexao);
            if (!produtoSalvo) {
                System.err.println("Erro ao inserir produto na cesta.");
                return false;
            }

            boolean estoqueAtualizado = estoqueDAO.reduzirEstoque(
                    prod.getProdutoProdCod(),
                    (int) prod.getProdQtd(), // Cast necessário
                    conexao
            );

            if (!estoqueAtualizado) {
                System.err.println("Erro ao atualizar estoque para produto " + prod.getProdutoProdCod());
                return false;
            }
        }

        System.out.println("✅ Cesta básica registrada com sucesso!");
        return true;
>>>>>>> 57019482546635da2438e3c2c2dd7bfea599315e
    }
}
