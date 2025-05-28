package projetoSalf.mvc.control;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import projetoSalf.mvc.dao.CategoriaProdutoDAO;
import projetoSalf.mvc.model.CategoriaProduto;

import java.util.List;

@Component
public class CategoriaProdutoControl {

    @Autowired
    private CategoriaProdutoDAO dao;

    public boolean adicionarCategoria(String descricao) {
        CategoriaProduto c = new CategoriaProduto();
        c.setDesc(descricao);
        return dao.inserir(c);
    }

    public List<CategoriaProduto> listarCategorias() {
        return dao.consultarTodos();
    }

    public boolean atualizarCategoria(int cod, String novaDesc) {
        CategoriaProduto c = new CategoriaProduto();
        c.setId(cod);
        c.setDesc(novaDesc);
        return dao.atualizar(c);
    }

    public boolean excluirCategoria(int cod) {
        return dao.excluir(cod);
    }

    public List<CategoriaProduto> buscarPorDescricao(String filtro) {
        return dao.buscarPorDescricao(filtro);
    }
}
