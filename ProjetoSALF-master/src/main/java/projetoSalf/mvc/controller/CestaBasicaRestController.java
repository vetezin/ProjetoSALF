package projetoSalf.mvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetoSalf.mvc.model.CestaBasica;
import projetoSalf.mvc.model.CbProd;
import projetoSalf.mvc.util.Conexao;
import projetoSalf.mvc.util.SingletonDB;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/apis/cesta")
public class CestaBasicaRestController {

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarCesta(@RequestBody Map<String, Object> dados) {
        try {
            Map<String, Object> cestaMap = (Map<String, Object>) dados.get("cesta");
            List<Map<String, Object>> produtosMap = (List<Map<String, Object>>) dados.get("listaProdutos");

            // Criar a cesta com base na sua classe final
            CestaBasica cesta = new CestaBasica();
            cesta.setCbMotivo((String) cestaMap.get("cb_motivo"));
            cesta.setCbDtCriacao(LocalDate.parse((String) cestaMap.get("cb_dtcriacao")));
            cesta.setCbDtDoacao(LocalDate.parse((String) cestaMap.get("cb_dtdoacao")));
            cesta.setCbCodFunc((int) cestaMap.get("cb_codfunc"));
            cesta.setPessoaCarentePcCod((int) cestaMap.get("cb_pessoacod"));

            // Montar os produtos da cesta
            List<CbProd> produtos = new ArrayList<>();
            for (Map<String, Object> p : produtosMap) {
                CbProd prod = new CbProd();
                prod.setProdutoProdCod((int) p.get("produto_prod_cod"));
                prod.setProdQtd((int) p.get("prod_qtd"));
                produtos.add(prod);
            }

            if (!SingletonDB.conectar()) {
                return ResponseEntity.status(500).body("❌ Erro ao conectar no banco.");
            }

            Conexao conexao = SingletonDB.getConexao();
            CestaBasicaController controller = new CestaBasicaController();
            boolean sucesso = controller.registrarCesta(cesta, produtos, conexao);

            if (sucesso) {
                return ResponseEntity.ok("✅ Cesta registrada com sucesso!");
            } else {
                return ResponseEntity.status(500).body("❌ Falha ao registrar cesta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erro no processamento da requisição.");
        }
    }
}
