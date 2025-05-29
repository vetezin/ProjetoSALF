package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ListaCompraDAO;

import java.util.List;

@Component
public class ListaCompra {

    private int id;
    private String descricao;
    private String dataCriacao; // <-- Continua como String
    private int funcionarioId;

    @Autowired // INJEÇÃO DO DAO AQUI, REPLICANDO O PADRÃO DE SAIDA
    private ListaCompraDAO dao;

    public ListaCompra() {}

    public ListaCompra(int id, String descricao, String dataCriacao, int funcionarioId) {
        this.id = id;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra(String descricao, String dataCriacao, int funcionarioId) {
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.funcionarioId = funcionarioId;
    }

    public ListaCompra(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(int funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    // MÉTODOS DE INTEGRAÇÃO COM DAO REPLICANDO O PADRÃO DE SAIDA
    public ListaCompra consultar(int id) {
        return dao.get(id);
    }

    public List<ListaCompra> consultar(String filtro) {
        return dao.get(filtro);
    }

    public boolean isEmpty(){
        // Para verificar se há registros no banco através do DAO,
        // o ideal seria um método 'count' ou verificar se 'get("")' retorna vazio.
        // Replicando o padrão de Saida, que usa dao.get("").isEmpty()
        return dao.get("").isEmpty();
    }

    // Renomeei o método para 'gravar' para ser mais consistente com a Saida
    public ListaCompra gravar(ListaCompra lista) {
        return dao.gravar(lista);
    }

    // O método 'alterar' na sua Saida recebe um objeto Saida.
    // O seu 'alterar' atual na ListaCompra usa 'this'.
    // Vou fazer uma versão que recebe 'this' e outra que recebe um objeto ListaCompra,
    // para maior flexibilidade, mas a que usa 'this' é mais direta ao replicar o seu original.
    public ListaCompra alterar() {
        return dao.alterar(this);
    }

    public ListaCompra alterar(ListaCompra lista) { // Adicionando uma sobrecarga se preferir passar o objeto
        return dao.alterar(lista);
    }

    // Renomeei o método para 'apagar' para ser mais consistente com a Saida
    public boolean apagar(ListaCompra lista) {
        return dao.apagar(lista);
    }
}