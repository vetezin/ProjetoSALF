package projetoSalf.mvc.model;

import projetoSalf.mvc.dao.CompraDAO;
import projetoSalf.mvc.dao.ProdutoCompraDAO;
import projetoSalf.mvc.util.Conexao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private int coCod;
    private String coDescricao;
    private float coValorTotal;
    private LocalDate coDtCompra;
    private int coTotalItens;
    private int funcionarioFuncCod;
    private int cotFornCotacaoCotCod;
    private int cotFornFornecedorFornCod;
    private boolean coConfirmada;
    private List<ProdutoCompra> itens;

    public Compra() {}

    public int getCoCod() { return coCod; }
    public void setCoCod(int coCod) { this.coCod = coCod; }

    public String getCoDescricao() { return coDescricao; }
    public void setCoDescricao(String coDescricao) { this.coDescricao = coDescricao; }

    public float getCoValorTotal() { return coValorTotal; }
    public void setCoValorTotal(float coValorTotal) { this.coValorTotal = coValorTotal; }

    public LocalDate getCoDtCompra() { return coDtCompra; }
    public void setCoDtCompra(LocalDate coDtCompra) { this.coDtCompra = coDtCompra; }

    public int getCoTotalItens() { return coTotalItens; }
    public void setCoTotalItens(int coTotalItens) { this.coTotalItens = coTotalItens; }

    public int getFuncionarioFuncCod() { return funcionarioFuncCod; }
    public void setFuncionarioFuncCod(int funcionarioFuncCod) { this.funcionarioFuncCod = funcionarioFuncCod; }

    public int getCotFornCotacaoCotCod() { return cotFornCotacaoCotCod; }
    public void setCotFornCotacaoCotCod(int cotFornCotacaoCotCod) { this.cotFornCotacaoCotCod = cotFornCotacaoCotCod; }

    public int getCotFornFornecedorFornCod() { return cotFornFornecedorFornCod; }
    public void setCotFornFornecedorFornCod(int cotFornFornecedorFornCod) { this.cotFornFornecedorFornCod = cotFornFornecedorFornCod; }

    public boolean isCoConfirmada() { return coConfirmada; }
    public void setCoConfirmada(boolean coConfirmada) { this.coConfirmada = coConfirmada; }

    public List<ProdutoCompra> getItens() { return itens; }
    public void setItens(List<ProdutoCompra> itens) { this.itens = itens; }

    public boolean inserir(Conexao conexao) {
        CompraDAO compraDAO = new CompraDAO();
        int codCompra = compraDAO.inserir(this, conexao);

        if (codCompra > 0 && this.itens != null) {
            ProdutoCompraDAO prodDAO = new ProdutoCompraDAO();

            for (ProdutoCompra pc : this.itens) {
                boolean inserido = prodDAO.inserir(pc, codCompra, conexao);
                if (!inserido) {
                    System.err.println("❌ Erro ao inserir item da compra.");
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public List<Compra> consultarTodasNaoConfirmadas(Conexao conexao) {
        CompraDAO dao = new CompraDAO();
        return dao.consultarNaoConfirmadas(conexao);
    }
}
