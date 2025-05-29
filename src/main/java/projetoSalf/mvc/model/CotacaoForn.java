package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.CotacaoFornDAO; // Importe o DAO de CotacaoForn

import java.util.List;

@Component
public class CotacaoForn {

    // Chaves compostas: cot_cod e forn_cod
    // Em JDBCTemplate ou com DAO manual, você gerencia isso.
    // Para ORMs como JPA, precisaria de uma classe @Embeddable para a chave composta.
    // Mantenho como campos separados para replicar o seu padrão de DAO.
    private int cot_cod; // Chave estrangeira para Cotacao
    private int forn_cod; // Chave estrangeira para Fornecedor

    @Autowired // INJEÇÃO DO DAO AQUI
    private CotacaoFornDAO dao;

    public CotacaoForn() {
        // Construtor padrão
    }

    // Construtor completo
    public CotacaoForn(int cot_cod, int forn_cod) {
        this.cot_cod = cot_cod;
        this.forn_cod = forn_cod;
    }

    // --- Getters e Setters ---
    public int getCot_cod() {
        return cot_cod;
    }

    public void setCot_cod(int cot_cod) {
        this.cot_cod = cot_cod;
    }

    public int getForn_cod() {
        return forn_cod;
    }

    public void setForn_cod(int forn_cod) {
        this.forn_cod = forn_cod;
    }

    // --- MÉTODOS DE INTEGRAÇÃO COM DAO REPLICANDO O PADRÃO ---
    // Para tabelas de associação, o método get pode precisar de ambas as chaves
    // ou pode retornar uma lista de associações por uma das chaves.
    // Aqui, crio um método 'get' que usa ambas, e um 'consultar' com filtro.
    public CotacaoForn get(int cot_cod, int forn_cod) {
        return dao.get(cot_cod, forn_cod);
    }

    public List<CotacaoForn> consultar(String filtro) {
        // O filtro aqui pode ser o cot_cod ou forn_cod, dependendo da sua necessidade no DAO
        return dao.get(filtro);
    }

    public boolean isEmpty() {
        return dao.get("").isEmpty();
    }

    // Gravar um novo relacionamento
    public CotacaoForn gravar(CotacaoForn cotacaoForn) {
        return dao.gravar(cotacaoForn);
    }

    // Alterar um relacionamento (raro para tabelas puramente de associação, mas seguindo o padrão)
    // Geralmente em tabelas de associação você insere ou remove, não altera.
    // Se houvesse outros campos na cot_forn além das chaves, o alterar faria sentido.
    public CotacaoForn alterar() {
        return dao.alterar(this);
    }

    public CotacaoForn alterar(CotacaoForn cotacaoForn) {
        return dao.alterar(cotacaoForn);
    }

    // Apagar um relacionamento específico
    public boolean apagar(CotacaoForn cotacaoForn) {
        return dao.apagar(cotacaoForn);
    }
}