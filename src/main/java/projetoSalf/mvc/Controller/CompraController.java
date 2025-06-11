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
        CompraDAO dao = new CompraDAO();
        return dao.listarTodas(); // Agora lista TODAS as compras, com o campo 'confirmada'
    }

    public Map<String, Object> addCompra(Compra compra) {
        Conexao conexao = new Conexao();
        boolean sucesso = compra.inserir(conexao);
        return sucesso
                ? Map.of("mensagem", "Compra registrada com sucesso")
                : Map.of("erro", "Erro ao registrar a compra");
    }

    public List<Map<String, Object>> getProdutosDaCompra(int compraId) {
        SingletonDB.conectar();
        Conexao conexao = SingletonDB.getConexao();
        ProdutoCompraDAO dao = new ProdutoCompraDAO();
        return dao.getProdutosDaCompra(compraId, conexao);
    }
}
