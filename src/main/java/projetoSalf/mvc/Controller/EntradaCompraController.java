package projetoSalf.mvc.Controller;

import org.springframework.stereotype.Service;
import projetoSalf.mvc.dao.EntradaCompraDAO;
import projetoSalf.mvc.dao.ProdutoCompraDAO;
import projetoSalf.mvc.model.ProdutoCompra;
import projetoSalf.mvc.util.Conexao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EntradaCompraController {

    public Map<String, Object> confirmarEstoque(Map<String, Object> payload) {
        Map<String, Object> resp = new HashMap<>();
        System.out.println("Payload recebido: " + payload);

        try {
            int compraId = ((Number) payload.get("compraId")).intValue();
            List<Map<String, Object>> itensData = (List<Map<String, Object>>) payload.get("itens");

            EntradaCompraDAO dao = new EntradaCompraDAO();
            ProdutoCompraDAO pcDao = new ProdutoCompraDAO(); // precisa ter função de buscar qtd atual

            Conexao conexao = new Conexao();

            for (Map<String, Object> item : itensData) {
                int prodCod = ((Number) item.get("prodCod")).intValue();
                int novaQtd = ((Number) item.get("qtd")).intValue();
                Object valorRaw = item.get("valorUnitario");
                if (valorRaw == null) {
                    return Map.of("erro", "Valor unitário ausente para o produto " + item.get("prodCod"));
                }
                float novoValor = ((Number) valorRaw).floatValue();

                int lcCod = ((Number) item.get("lcCod")).intValue();
                int cotCod = ((Number) item.get("cotCod")).intValue();
                int fornCod = ((Number) item.get("fornCod")).intValue();

                int qtdAtual = pcDao.buscarQtdAtual(compraId, prodCod, lcCod, cotCod, fornCod, conexao);

                int diferenca = novaQtd - qtdAtual;

                boolean atualizou = pcDao.atualizarItemCompra(compraId, prodCod, lcCod, cotCod, fornCod, novaQtd, novoValor);
                if (!atualizou) return Map.of("erro", "Erro ao atualizar item da compra.");

                if (diferenca > 0) {
                    boolean ok = dao.incrementarEstoque(prodCod, diferenca);
                    if (!ok) return Map.of("erro", "Erro ao atualizar estoque do produto " + prodCod);
                }
            }

            dao.marcarCompraComoConfirmada(compraId);
            return Map.of("sucesso", true, "compraId", compraId, "mensagem", "✅ Estoque atualizado com sucesso!");

        } catch (Exception e) {
            return Map.of("erro", "Erro ao confirmar estoque: " + e.getMessage());
        }
    }


    public Map<String, Object> desfazerEstoque(Map<String, Object> dados) {
        try {
            int compraId = ((Number) dados.get("compraId")).intValue();
            List<Map<String, Object>> itens = (List<Map<String, Object>>) dados.get("itens");

            EntradaCompraDAO entradaDAO = new EntradaCompraDAO();
            System.out.println("📦 Dados recebidos no desfazerEstoque:");
            System.out.println(dados);

            List<ProdutoCompra> produtos = new ArrayList<>();
            for (Map<String, Object> item : itens) {
                System.out.println("🔹 Item:");
                System.out.println(item);

                ProdutoCompra p = new ProdutoCompra();
                p.setProdCod(((Number) item.get("prodCod")).intValue());
                p.setLcCod(((Number) item.get("lcCod")).intValue());
                p.setCotCod(((Number) item.get("cotCod")).intValue());
                p.setFornCod(((Number) item.get("fornCod")).intValue());
                produtos.add(p);
            }


            boolean ok = entradaDAO.desfazerEntradaCompra(compraId, produtos);
            if (!ok) {
                return Map.of("erro", "❌ Falha ao desfazer entrada de estoque.");
            }

            return Map.of("mensagem", "🧹 Estoque revertido com sucesso e compra desmarcada.");

        } catch (Exception e) {
            return Map.of("erro", "Erro interno: " + e.getMessage());
        }
    }


}
