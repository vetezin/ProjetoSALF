package projetoSalf.mvc.dao;

import projetoSalf.mvc.util.Conexao;
import java.util.List;

public interface IDAO<T> {
    public T gravar(T entidade, Conexao conexao);
    public T alterar(T entidade, Conexao conexao);
    public boolean apagar(T entidade, Conexao conexao);
    public T get(int id, Conexao conexao);
    public List<T> get(String filtro, Conexao conexao);
}
