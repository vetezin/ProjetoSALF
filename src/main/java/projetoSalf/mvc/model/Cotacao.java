package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.CotacaoDAO; // Importe o DAO de Cotacao

import java.util.List;

@Component
public class Cotacao {

    private int cot_cod; // Corresponde a cot_cod
    private String cot_dtabertura; // Corresponde a cot_dtabertura (mantido como String)
    private String cot_dtfechamento; // Corresponde a cot_dtfechamento (mantido como String)
    private int lc_cod; // Corresponde a lc_cod (chave estrangeira para ListaCompra)

    @Autowired // INJEÇÃO DO DAO AQUI, REPLICANDO O PADRÃO DE SAIDA
    private CotacaoDAO dao;

    public Cotacao() {
        // Construtor padrão necessário para Spring e frameworks de serialização
    }

    // Construtor completo
    public Cotacao(int cot_cod, String cot_dtabertura, String cot_dtfechamento, int lc_cod) {
        this.cot_cod = cot_cod;
        this.cot_dtabertura = cot_dtabertura;
        this.cot_dtfechamento = cot_dtfechamento;
        this.lc_cod = lc_cod;
    }

    // Construtor para gravação (sem ID, assumindo que o banco gera)
    public Cotacao(String cot_dtabertura, String cot_dtfechamento, int lc_cod) {
        this.cot_dtabertura = cot_dtabertura;
        this.cot_dtfechamento = cot_dtfechamento;
        this.lc_cod = lc_cod;
    }

    // --- Getters e Setters ---
    public int getCot_cod() {
        return cot_cod;
    }

    public void setCot_cod(int cot_cod) {
        this.cot_cod = cot_cod;
    }

    public String getCot_dtabertura() {
        return cot_dtabertura;
    }

    public void setCot_dtabertura(String cot_dtabertura) {
        this.cot_dtabertura = cot_dtabertura;
    }

    public String getCot_dtfechamento() {
        return cot_dtfechamento;
    }

    public void setCot_dtfechamento(String cot_dtfechamento) {
        this.cot_dtfechamento = cot_dtfechamento;
    }

    public int getLc_cod() {
        return lc_cod;
    }

    public void setLc_cod(int lc_cod) {
        this.lc_cod = lc_cod;
    }

    // --- MÉTODOS DE INTEGRAÇÃO COM DAO REPLICANDO O PADRÃO ---
    public Cotacao consultar(int id) {
        return dao.get(id);
    }

    // Assumindo que você pode querer consultar por algum filtro (ex: data, lista de compra)
    // Se não houver filtro, dao.get("") retornaria todos.
    public List<Cotacao> consultar(String filtro) {
        return dao.get(filtro);
    }

    public boolean isEmpty() {
        return dao.get("").isEmpty();
    }

    public Cotacao gravar(Cotacao cotacao) {
        return dao.gravar(cotacao);
    }

    public Cotacao alterar() {
        return dao.alterar(this);
    }

    public Cotacao alterar(Cotacao cotacao) {
        return dao.alterar(cotacao);
    }

    public boolean apagar(Cotacao cotacao) {
        return dao.apagar(cotacao);
    }
}