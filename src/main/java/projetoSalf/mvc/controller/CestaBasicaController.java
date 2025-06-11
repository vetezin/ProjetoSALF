package projetoSalf.mvc.controller;

import projetoSalf.mvc.dao.CestaBasicaDAO;
import projetoSalf.mvc.dao.CbProdDAO;
import projetoSalf.mvc.dao.EstoqueCestaDAO;
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

public class CestaBasicaController {

    public boolean registrarCesta(CestaBasica cesta, List<CbProd> listaProdutos, Conexao conexao) {
        CestaBasicaDAO cestaDAO = new CestaBasicaDAO();
        CbProdDAO cbProdDAO = new CbProdDAO();
        EstoqueCestaDAO estoqueDAO = new EstoqueCestaDAO();

        // Insere a cesta
        boolean cestaSalva = cestaDAO.inserir(cesta, conexao);
        if (!cestaSalva) {
            System.err.println("Erro ao salvar a cesta básica.");
            return false;
        }

        // Recupera o ID da cesta recém-cadastrada
        int idCesta = conexao.getMaxPK("cesta_basica", "cb_cod");

        // Recupera o maior ID atual da cb_prod
        int ultimoId = conexao.getMaxPK("cb_prod", "cb_prod_id");

        for (CbProd prod : listaProdutos) {
            // Gera novo ID para cb_prod
            int novoId = ++ultimoId;
            prod.setCb_prod_id(novoId);

            // Define a qual cesta este produto pertence
            prod.setCesta_basica_cb_cod(idCesta);

            // Insere o produto na cesta
            boolean produtoSalvo = cbProdDAO.inserir(prod, conexao);
            if (!produtoSalvo) {
                System.err.println("Erro ao inserir produto na cesta.");
                return false;
            }

            // Atualiza o estoque
            boolean estoqueAtualizado = estoqueDAO.reduzirEstoque(prod.getProduto_prod_cod(), prod.getProd_qtd(), conexao);
            if (!estoqueAtualizado) {
                System.err.println("Erro ao atualizar estoque para produto " + prod.getProduto_prod_cod());
                return false;
            }
        }

        System.out.println("✅ Cesta básica registrada com sucesso!");
        return true;
    }
}
