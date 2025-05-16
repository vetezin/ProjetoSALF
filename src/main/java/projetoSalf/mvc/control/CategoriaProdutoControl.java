package projetoSalf.mvc.control;

import projetoSalf.mvc.dao.CategoriaProdutoDAO;
import projetoSalf.mvc.model.CategoriaProduto;

import java.util.List;

public class CategoriaProdutoControl {

    private CategoriaProdutoDAO dao;

    public CategoriaProdutoControl() {
        dao = new CategoriaProdutoDAO();
    }

    public boolean adicionarCategoria(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            System.out.println("Descrição não pode ser vazia.");
            return false;
        }

        CategoriaProduto cat = new CategoriaProduto();
        cat.setCat_desc(descricao);
        dao.inserir(cat);
        return true;
    }

    public boolean atualizarCategoria(int cod, String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            System.out.println("Descrição não pode ser vazia.");
            return false;
        }

        CategoriaProduto cat = new CategoriaProduto(cod, descricao);
        dao.atualizar(cat);
        return true;
    }

    public boolean excluirCategoria(int cod) {
        if (cod <= 0) {
            System.out.println("Código inválido.");
            return false;
        }

        dao.excluir(cod);
        return true;
    }

    public List<CategoriaProduto> listarCategorias() {
        return dao.listar();
    }

    public List<CategoriaProduto> buscarPorDescricao(String filtro) {
        return dao.buscarPorDescricao(filtro);
    }
}
