package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.CompraDAO;
import projetoSalf.mvc.dao.ProdutoCompraDAO;
import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.model.Fornecedor;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.util.*;

@Service
public class CompraController {
    public List<Map<String, Object>> getCompras() {
        SingletonDB.conectar();
        Conexao conexao = SingletonDB.getConexao();
        List<Compra> lista = new CompraDAO().consultarNaoConfirmadas(conexao);

        List<Map<String, Object>> resposta = new ArrayList<>();
        for (Compra c : lista) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getCoCod());
            item.put("descricao", c.getCoDescricao());
            item.put("valorTotal", c.getCoValorTotal());
            item.put("data", c.getCoDtCompra());
            item.put("totalItens", c.getCoTotalItens());
            item.put("funcionario", c.getFuncionarioFuncCod());
            item.put("cotacaoCod", c.getCotFornCotacaoCotCod());
            item.put("fornecedorCod", c.getCotFornFornecedorFornCod());
            Fornecedor forn = Fornecedor.consultarPorId(c.getCotFornFornecedorFornCod());
            item.put("fornecedorNome", forn != null ? forn.getForn_nome() : "Desconhecido");
            resposta.add(item);
        }
        return resposta;
    }

    public Map<String, Object> addCompra(Compra compra) {
        Conexao conexao = new Conexao();
        boolean sucesso = compra.inserir(conexao);
        return sucesso ? Map.of("mensagem", "Compra registrada com sucesso")
                : Map.of("erro", "Erro ao registrar a compra");
    }

    public List<Map<String, Object>> getProdutosDaCompra(int compraId) {
        SingletonDB.conectar();
        Conexao conexao = SingletonDB.getConexao();
        ProdutoCompraDAO dao = new ProdutoCompraDAO();
        return dao.getProdutosDaCompra(compraId, conexao);
    }
}
