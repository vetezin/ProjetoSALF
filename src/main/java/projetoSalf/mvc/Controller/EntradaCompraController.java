package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.EntradaCompraDAO;
import projetoSalf.mvc.model.Compra;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.util.Map;

@Service
public class EntradaCompraController {

    public Map<String, Object> registrarEntradaCompra(Compra compra) {
        SingletonDB.conectar(); // garante a conexão ativa
        Conexao conexao = SingletonDB.getConexao();
        boolean sucesso = compra.inserir(conexao);


        if (sucesso) {
            EntradaCompraDAO entradaDAO = new EntradaCompraDAO();
            boolean estoqueOk = entradaDAO.registrarEntrada(compra);
            if (estoqueOk) {
                return Map.of("mensagem", "Compra registrada e estoque atualizado com sucesso!");
            } else {
                return Map.of("erro", "Compra registrada, mas falha ao atualizar estoque.");
            }
        } else {
            return Map.of("erro", "Erro ao registrar a compra.");
        }
    }
}
