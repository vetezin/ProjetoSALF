package projetoSalf.mvc.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import projetoSalf.mvc.dao.ListaCompraProdutoDAO; // Mantenha a importação do DAO

// Removi o import java.util.List; pois não há métodos que retornam List, replicando o padrão da SaidaProd


@Component // 1. Anotação @Component
public class ListaCompraProduto {
    private int produtoCod; // 2. Atributos private
    private int listaCompraCod;
    private int qtd;

    @Autowired // 3. Injeção do DAO
    private ListaCompraProdutoDAO dao;

    public ListaCompraProduto() {} // 4. Construtor vazio

    public ListaCompraProduto(int produtoCod, int listaCompraCod, int qtd) { // 5. Construtor com todos os atributos
        this.produtoCod = produtoCod;
        this.listaCompraCod = listaCompraCod;
        this.qtd = qtd;
    }

    // --- Getters e Setters (Mantidos, pois fazem parte do padrão) ---
    public int getProdutoCod() { // 6. Getters
        return produtoCod;
    }

    public void setProdutoCod(int produtoCod) { // 6. Setters
        this.produtoCod = produtoCod;
    }

    public int getListaCompraCod() {
        return listaCompraCod;
    }

    public void setListaCompraCod(int listaCompraCod) {
        this.listaCompraCod = listaCompraCod;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    // --- Métodos de CRUD (APENAS gravar, alterar, deletar), replicando o padrão de SaidaProd ---

    public ListaCompraProduto gravar(ListaCompraProduto lcp) { // 7. Método gravar
        return dao.gravar(lcp);
    }

    public ListaCompraProduto alterar(ListaCompraProduto lcp) { // 7. Método alterar
        return dao.alterar(lcp);
    }

    public boolean deletar(ListaCompraProduto lcp) { // 7. Método deletar
        // Replicando o uso de 'this' no método 'deletar' da SaidaProd original,
        // que usava 'return dao.apagar(this);'
        return dao.apagar(this);
    }

    // 8. Removido: O método 'consultarPorListaCompra' foi removido para replicar fielmente o padrão
    // da SaidaProd que você forneceu, que não tinha métodos de consulta além dos CRUD básicos.
    /*
    // Método original que foi removido para alinhar ao padrão de SaidaProd
    public List<ListaCompraProduto> consultarPorListaCompra(int listaCompraCod) {
        return dao.getPorListaCompra(listaCompraCod);
    }
    */
}