package projetoSalf.mvc.model;

import projetoSalf.mvc.dao.CompraDAO;
import projetoSalf.mvc.dao.ProdutoCompraDAO;
import projetoSalf.mvc.util.Conexao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Compra {
    private int coCod;
    private String coDescricao;
    private float coValorTotal;
    private LocalDate coDtCompra;
    private int coTotalItens;
    private int funcionarioFuncCod;
    private int cotFornCotacaoCotCod;
    private int cotFornFornecedorFornCod;
    private List<ProdutoCompra> itens;

    public Compra() {}

    // Getters e Setters omitidos por brevidade...


    public int getCoCod() {
        return coCod;
    }

    public void setCoCod(int coCod) {
        this.coCod = coCod;
    }

    public String getCoDescricao() {
        return coDescricao;
    }

    public void setCoDescricao(String coDescricao) {
        this.coDescricao = coDescricao;
    }

    public float getCoValorTotal() {
        return coValorTotal;
    }

    public void setCoValorTotal(float coValorTotal) {
        this.coValorTotal = coValorTotal;
    }

    public LocalDate getCoDtCompra() {
        return coDtCompra;
    }

    public void setCoDtCompra(LocalDate coDtCompra) {
        this.coDtCompra = coDtCompra;
    }

    public int getCoTotalItens() {
        return coTotalItens;
    }

    public void setCoTotalItens(int coTotalItens) {
        this.coTotalItens = coTotalItens;
    }

    public int getFuncionarioFuncCod() {
        return funcionarioFuncCod;
    }

    public void setFuncionarioFuncCod(int funcionarioFuncCod) {
        this.funcionarioFuncCod = funcionarioFuncCod;
    }

    public int getCotFornCotacaoCotCod() {
        return cotFornCotacaoCotCod;
    }

    public void setCotFornCotacaoCotCod(int cotFornCotacaoCotCod) {
        this.cotFornCotacaoCotCod = cotFornCotacaoCotCod;
    }

    public int getCotFornFornecedorFornCod() {
        return cotFornFornecedorFornCod;
    }

    public void setCotFornFornecedorFornCod(int cotFornFornecedorFornCod) {
        this.cotFornFornecedorFornCod = cotFornFornecedorFornCod;
    }

    public List<ProdutoCompra> getItens() {
        return itens;
    }

    public void setItens(List<ProdutoCompra> itens) {
        this.itens = itens;
    }

    public static Compra fromMap(Map<String, Object> map) {
        Compra c = new Compra();
        c.setCoDescricao((String) map.get("coDescricao"));
        c.setCoValorTotal(Float.parseFloat(map.get("coValorTotal").toString()));
        c.setCoDtCompra(LocalDate.parse((String) map.get("coDtCompra")));
        c.setCoTotalItens(Integer.parseInt(map.get("coTotalItens").toString()));
        c.setFuncionarioFuncCod(Integer.parseInt(map.get("funcionarioFuncCod").toString()));
        c.setCotFornCotacaoCotCod(Integer.parseInt(map.get("cotFornCotacaoCotCod").toString()));
        c.setCotFornFornecedorFornCod(Integer.parseInt(map.get("cotFornFornecedorFornCod").toString()));
        return c;
    }

    public void fromList(List<Map<String, Object>> listaProdutos) {
        this.itens = new ArrayList<>();
        for (Map<String, Object> p : listaProdutos) {
            ProdutoCompra pc = ProdutoCompra.fromMap(p);
            this.itens.add(pc);
        }
    }

    public boolean inserir(Conexao conexao) {
        CompraDAO compraDAO = new CompraDAO();
        int codCompra = compraDAO.inserir(this, conexao);  // <- AQUI está falhando e retornando -1

        if (codCompra > 0 && this.itens != null) {
            ProdutoCompraDAO prodDAO = new ProdutoCompraDAO();
            for (ProdutoCompra pc : this.itens) {
                prodDAO.inserir(pc, codCompra,conexao);
            }
            return true;
        }
        return false;  // <- Está caindo aqui, por isso aparece "Erro ao registrar a compra"
    }



    public List<Compra> consultarTodas(Conexao conexao) {
        CompraDAO dao = new CompraDAO();
        return dao.consultarTodas(conexao);
    }

}
