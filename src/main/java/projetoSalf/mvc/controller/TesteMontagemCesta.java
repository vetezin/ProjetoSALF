package projetoSalf.mvc.controller;

import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TesteMontagemCesta {
    public static void main(String[] args) {
        // 1. Conecta ao banco
        if (!SingletonDB.conectar()) {
            System.err.println("❌ Erro ao conectar no banco de dados!");
            return;
        }

        Conexao conexao = SingletonDB.getConexao();

        // 2. Cria a cesta básica
        CestaBasica cesta = new CestaBasica();
        cesta.setCb_motivo("Teste automatizado");
        cesta.setCb_dtcriacao(Date.valueOf("2025-05-26")); // ou new Date(System.currentTimeMillis())
        cesta.setCb_dtdoacao(Date.valueOf("2025-05-26"));
        cesta.setCb_codfunc(1); // ID do funcionário
        cesta.setPessoa_carente_pc_cod(1); // ID da pessoa carente

        // 3. Cria os produtos que vão na cesta
        List<CbProd> produtos = new ArrayList<>();

        CbProd prod1 = new CbProd();
        prod1.setProduto_prod_cod(1); // ID do produto
        prod1.setProd_qtd(2);         // Quantidade

        CbProd prod2 = new CbProd();
        prod2.setProduto_prod_cod(2); // Outro produto
        prod2.setProd_qtd(1);

        produtos.add(prod1);
        produtos.add(prod2);

        // 4. Chama o controller
        CestaBasicaController controller = new CestaBasicaController();
        boolean sucesso = controller.registrarCesta(cesta, produtos, conexao);

        if (sucesso) {
            System.out.println("✅ Teste finalizado com sucesso!");
        } else {
            System.err.println("❌ Algo deu errado no teste.");
        }
    }
}
