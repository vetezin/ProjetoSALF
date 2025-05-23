package projetoSalf.mvc.dao;

import org.springframework.stereotype.Repository;
import projetoSalf.mvc.model.Itens;
import projetoSalf.mvc.util.SingletonDB;

import java.util.List;

@Repository
public class ItensDAO implements IDAO<Itens>{

    @Override
    public Object gravar(Itens itens) {
        String sql = """
            INSERT INTO lc_prod(lc_prod_qtd, lc_cod, prod_cod)
            VALUES ('#1', '#2', '#2');
        """;
        sql = sql.replace("#1", String.valueOf(itens.getQtd()))
                .replace("#2", String.valueOf(itens.getLc_cod()))
                .replace("#3", String.valueOf(itens.getProdutoCod()));

        if (SingletonDB.getConexao().manipular(sql)) {
            return itens;
        } else {
            System.out.println("Erro: " + SingletonDB.getConexao().getMensagemErro());
            return null;
        }
    }

    @Override
    public Object alterar(Itens entidade) {
        return null;
    }

    @Override
    public boolean apagar(Itens entidade) {
        return false;
    }

    @Override
    public Itens get(int id) {
        return null;
    }

    @Override
    public List<Itens> get(String filtro) {
        return List.of();
    }

}
