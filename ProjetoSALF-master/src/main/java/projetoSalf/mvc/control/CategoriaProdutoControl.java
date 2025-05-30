package projetoSalf.mvc.control;

import projetoSalf.mvc.dao.CategoriaDAO;
import projetoSalf.mvc.model.Categoria;

import java.util.List;

public class CategoriaProdutoControl {

    private CategoriaDAO dao;

    public CategoriaProdutoControl() {
        dao = new CategoriaDAO();
    }

    public boolean adicionarCategoria(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            System.out.println("Descrição não pode ser vazia.");
            return false;
        }

        Categoria cat = new Categoria();
        cat.setDesc(descricao);
        dao.gravar(cat);
        return true;
    }

    public boolean atualizarCategoria(int cod, String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            System.out.println("Descrição não pode ser vazia.");
            return false;
        }

        Categoria cat = new Categoria(cod, descricao);
        dao.alterar(cat);
        return true;
    }

    public boolean excluirCategoria(int cod) {
        if (cod <= 0) {
            System.out.println("Código inválido.");
            return false;
        }

        Categoria cat = new Categoria(cod);
        dao.apagar(cat);
        return true;
    }

    public List<Categoria> listarCategorias() {
        return dao.get("");
    }

    public List<Categoria> buscarPorDescricao(String filtro) {
        return dao.get("LOWER(cat_desc) LIKE LOWER('%" + filtro + "%')");
    }
}
