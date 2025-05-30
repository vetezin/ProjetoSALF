package projetoSalf.mvc.controller;

import projetoSalf.mvc.dao.CestaBasicaDAO;
import projetoSalf.mvc.dao.CbProdDAO;
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;

import java.sql.Connection;
import java.util.List;

public class CestaBasicaController {

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
    }
}
