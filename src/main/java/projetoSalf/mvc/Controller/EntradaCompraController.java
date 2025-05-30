package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.EntradaCompraDAO;

import java.util.List;
import java.util.Map;

@Service
public class EntradaCompraController {

    // NÃO USAMOS MAIS ESSE AQUI ↓
    public Map<String, Object> registrarEntradaCompraFakeQueDuplicaCompra() {
        return Map.of("erro", "Não usar esse método pra confirmar estoque.");
    }

    public Map<String, Object> confirmarEstoque(Map<String, Object> dados) {
        try {
            int compraId = (int) dados.get("compraId");
            List<Map<String, Object>> itens = (List<Map<String, Object>>) dados.get("itens");

            EntradaCompraDAO entradaDAO = new EntradaCompraDAO();

            for (Map<String, Object> item : itens) {
                int prodCod = (int) item.get("prodCod");
                int qtd = (int) item.get("qtd");

                boolean ok = entradaDAO.incrementarEstoque(prodCod, qtd);
                if (!ok) {
                    return Map.of("erro", "❌ Falha ao atualizar estoque do produto " + prodCod);
                }
            }

            boolean sucesso = entradaDAO.marcarCompraComoConfirmada(compraId);
            if (!sucesso) {
                return Map.of("erro", "⚠️ Estoque atualizado, mas não foi possível marcar a compra como confirmada.");
            }

            return Map.of("mensagem", "✅ Estoque da compra #" + compraId + " atualizado e compra marcada como confirmada!");
        } catch (Exception e) {
            return Map.of("erro", "Erro ao processar entrada: " + e.getMessage());
        }
    }
}
