package projetoSalf.mvc.dao;

import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.Conexao;

import java.util.List;

public interface IDAO<T>{
    public Object gravar(T entidade);
    public Object alterar(T entidade);
    public boolean apagar(T entidade);
    public T get(int id);
    public List<T> get(String filtro);
}
